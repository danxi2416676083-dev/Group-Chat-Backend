package com.groupchat.websocket;

import com.alibaba.fastjson2.JSON;
import com.groupchat.entity.Message;
import com.groupchat.repository.GroupMemberRepository;
import com.groupchat.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * WebSocket服务器
 * 
 * 说明:
 * 1. 处理WebSocket连接的建立、消息收发、断开
 * 2. 管理用户连接状态
 * 3. 实现消息的实时推送
 * 
 * 连接URL: ws://localhost:8080/api/ws/chat?token={jwt_token}
 * 
 * @author GroupChat Team
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketServer extends TextWebSocketHandler {

    /**
     * JWT Token提供者
     */
    private final JwtTokenProvider tokenProvider;

    /**
     * 群组成员数据访问层
     */
    private final GroupMemberRepository groupMemberRepository;

    /**
     * 存储所有WebSocket会话
     * Key: 用户ID, Value: WebSocketSession
     */
    private static final Map<Long, WebSocketSession> USER_SESSIONS = new ConcurrentHashMap<>();

    /**
     * 存储群组会话映射
     * Key: 群组ID, Value: 在线用户ID列表
     */
    private static final Map<Long, List<Long>> GROUP_USERS = new ConcurrentHashMap<>();

    /**
     * 连接建立后触发
     * 
     * @param session WebSocket会话
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 从URL参数获取Token
        String token = getTokenFromSession(session);
        
        if (token == null || !tokenProvider.validateToken(token)) {
            log.warn("WebSocket连接认证失败");
            session.close(CloseStatus.POLICY_VIOLATION);
            return;
        }
        
        // 获取用户ID
        Long userId = tokenProvider.getUserIdFromToken(token);
        String username = tokenProvider.getUsernameFromToken(token);
        
        // 存储会话
        USER_SESSIONS.put(userId, session);
        
        // 设置用户ID到会话属性
        session.getAttributes().put("userId", userId);
        session.getAttributes().put("username", username);
        
        log.info("WebSocket连接建立: userId={}, username={}", userId, username);
        
        // 发送连接成功消息
        sendMessageToUser(userId, WebSocketMessage.connectSuccess(userId, username));
        
        // 广播用户上线通知给好友
        broadcastUserStatus(userId, true);
    }

    /**
     * 收到客户端消息后触发
     * 
     * @param session WebSocket会话
     * @param message 文本消息
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        String payload = message.getPayload();
        
        log.debug("收到WebSocket消息: userId={}, payload={}", userId, payload);
        
        try {
            // 解析消息
            WebSocketMessage wsMessage = JSON.parseObject(payload, WebSocketMessage.class);
            
            // 处理不同类型的消息
            switch (wsMessage.getType()) {
                case "ping":
                    // 心跳响应
                    sendMessageToUser(userId, WebSocketMessage.pong());
                    break;
                    
                case "join_group":
                    // 加入群组
                    handleJoinGroup(userId, wsMessage.getGroupId());
                    break;
                    
                case "leave_group":
                    // 离开群组
                    handleLeaveGroup(userId, wsMessage.getGroupId());
                    break;
                    
                case "typing":
                    // 正在输入通知
                    handleTyping(userId, wsMessage);
                    break;
                    
                default:
                    log.warn("未知的WebSocket消息类型: {}", wsMessage.getType());
            }
        } catch (Exception e) {
            log.error("处理WebSocket消息失败", e);
            sendMessageToUser(userId, WebSocketMessage.error("消息格式错误"));
        }
    }

    /**
     * 连接关闭后触发
     * 
     * @param session WebSocket会话
     * @param status 关闭状态
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        
        if (userId != null) {
            // 从所有群组中移除
            GROUP_USERS.forEach((groupId, users) -> users.remove(userId));
            
            // 移除会话
            USER_SESSIONS.remove(userId);
            
            log.info("WebSocket连接关闭: userId={}, status={}", userId, status);
            
            // 广播用户离线通知
            broadcastUserStatus(userId, false);
        }
    }

    /**
     * 传输错误时触发
     * 
     * @param session WebSocket会话
     * @param exception 异常
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        log.error("WebSocket传输错误: userId={}", userId, exception);
        
        if (session.isOpen()) {
            session.close(CloseStatus.SERVER_ERROR);
        }
    }

    /**
     * 发送消息给指定用户
     * 
     * @param userId 用户ID
     * @param message 消息对象
     * @return 是否发送成功
     */
    public boolean sendMessageToUser(Long userId, WebSocketMessage message) {
        WebSocketSession session = USER_SESSIONS.get(userId);
        
        if (session != null && session.isOpen()) {
            try {
                String payload = JSON.toJSONString(message);
                session.sendMessage(new TextMessage(payload));
                return true;
            } catch (IOException e) {
                log.error("发送WebSocket消息失败: userId={}", userId, e);
            }
        }
        return false;
    }

    /**
     * 发送消息到群组(广播)
     * 
     * @param message 消息实体
     */
    public void sendMessage(Message message) {
        if (message.getSessionType() == 1 && message.getGroupId() != null) {
            // 群聊消息
            Long groupId = message.getGroupId();
            
            // 获取群组成员
            List<Long> memberIds = groupMemberRepository.findByGroupId(groupId)
                .stream()
                .map(m -> m.getUserId())
                .toList();
            
            // 构建WebSocket消息
            WebSocketMessage wsMessage = WebSocketMessage.chatMessage(message);
            
            // 发送给所有在线成员
            int sentCount = 0;
            for (Long memberId : memberIds) {
                if (!memberId.equals(message.getSenderId())) {  // 不发送给自己
                    if (sendMessageToUser(memberId, wsMessage)) {
                        sentCount++;
                    }
                }
            }
            
            log.debug("群聊消息推送: groupId={}, 在线成员数={}, 推送成功={}", 
                groupId, memberIds.size(), sentCount);
        } else {
            // 单聊消息
            if (message.getReceiverId() != null) {
                WebSocketMessage wsMessage = WebSocketMessage.chatMessage(message);
                sendMessageToUser(message.getReceiverId(), wsMessage);
            }
        }
    }

    /**
     * 通知消息撤回
     * 
     * @param message 被撤回的消息
     */
    public void notifyMessageRecalled(Message message) {
        WebSocketMessage wsMessage = WebSocketMessage.recallMessage(
            message.getMsgId(), 
            message.getSenderId(),
            message.getGroupId()
        );
        
        if (message.getSessionType() == 1 && message.getGroupId() != null) {
            // 群聊撤回通知
            List<Long> memberIds = groupMemberRepository.findByGroupId(message.getGroupId())
                .stream()
                .map(m -> m.getUserId())
                .toList();
            
            for (Long memberId : memberIds) {
                sendMessageToUser(memberId, wsMessage);
            }
        } else {
            // 单聊撤回通知
            sendMessageToUser(message.getReceiverId(), wsMessage);
        }
    }

    /**
     * 处理加入群组
     * 
     * @param userId 用户ID
     * @param groupId 群组ID
     */
    private void handleJoinGroup(Long userId, Long groupId) {
        if (groupId == null) return;
        
        // 添加到群组用户列表
        GROUP_USERS.computeIfAbsent(groupId, k -> new CopyOnWriteArrayList<>()).add(userId);
        
        log.debug("用户 {} 加入群组 {} 的WebSocket频道", userId, groupId);
        
        // 发送确认消息
        sendMessageToUser(userId, WebSocketMessage.groupJoined(groupId));
    }

    /**
     * 处理离开群组
     * 
     * @param userId 用户ID
     * @param groupId 群组ID
     */
    private void handleLeaveGroup(Long userId, Long groupId) {
        if (groupId == null) return;
        
        // 从群组用户列表移除
        List<Long> users = GROUP_USERS.get(groupId);
        if (users != null) {
            users.remove(userId);
        }
        
        log.debug("用户 {} 离开群组 {} 的WebSocket频道", userId, groupId);
    }

    /**
     * 处理正在输入通知
     * 
     * @param userId 用户ID
     * @param wsMessage WebSocket消息
     */
    private void handleTyping(Long userId, WebSocketMessage wsMessage) {
        // 转发给接收者或群组成员
        if (wsMessage.getGroupId() != null) {
            // 群聊输入状态
            List<Long> memberIds = groupMemberRepository.findByGroupId(wsMessage.getGroupId())
                .stream()
                .map(m -> m.getUserId())
                .filter(id -> !id.equals(userId))
                .toList();
            
            WebSocketMessage typingMsg = WebSocketMessage.typing(userId, wsMessage.getGroupId(), null);
            for (Long memberId : memberIds) {
                sendMessageToUser(memberId, typingMsg);
            }
        } else if (wsMessage.getReceiverId() != null) {
            // 单聊输入状态
            WebSocketMessage typingMsg = WebSocketMessage.typing(userId, null, wsMessage.getReceiverId());
            sendMessageToUser(wsMessage.getReceiverId(), typingMsg);
        }
    }

    /**
     * 广播用户在线状态
     * 
     * @param userId 用户ID
     * @param online 是否在线
     */
    private void broadcastUserStatus(Long userId, boolean online) {
        WebSocketMessage statusMsg = WebSocketMessage.userStatus(userId, online);
        
        // 这里简化处理，实际应该发送给好友
        // 遍历所有在线用户发送状态更新
        USER_SESSIONS.keySet().forEach(onlineUserId -> {
            if (!onlineUserId.equals(userId)) {
                sendMessageToUser(onlineUserId, statusMsg);
            }
        });
    }

    /**
     * 从会话中获取Token
     * 
     * @param session WebSocket会话
     * @return Token字符串
     */
    private String getTokenFromSession(WebSocketSession session) {
        // 从URL参数获取
        String query = session.getUri().getQuery();
        if (query != null && query.contains("token=")) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("token=")) {
                    return param.substring(6);
                }
            }
        }
        return null;
    }

    /**
     * 检查用户是否在线
     * 
     * @param userId 用户ID
     * @return 在线返回true
     */
    public boolean isUserOnline(Long userId) {
        WebSocketSession session = USER_SESSIONS.get(userId);
        return session != null && session.isOpen();
    }

    /**
     * 获取在线用户数量
     * 
     * @return 在线用户数
     */
    public int getOnlineUserCount() {
        return USER_SESSIONS.size();
    }
}
