package com.groupchat.websocket;

import com.groupchat.entity.Message;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * WebSocket消息对象
 * 
 * 说明:
 * 1. 用于WebSocket通信的消息格式
 * 2. 支持多种消息类型
 * 3. 统一前后端通信协议
 * 
 * @author GroupChat Team
 */
@Data
public class WebSocketMessage {

    /**
     * 消息类型
     * - connect_success: 连接成功
     * - chat_message: 聊天消息
     * - recall_message: 撤回消息
     * - user_status: 用户在线状态
     * - typing: 正在输入
     * - group_joined: 加入群组确认
     * - pong: 心跳响应
     * - error: 错误消息
     */
    private String type;

    /**
     * 发送者ID
     */
    private Long senderId;

    /**
     * 发送者昵称
     */
    private String senderName;

    /**
     * 接收者ID(单聊)
     */
    private Long receiverId;

    /**
     * 群组ID(群聊)
     */
    private Long groupId;

    /**
     * 消息内容
     */
    private Object data;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 构造方法
     */
    public WebSocketMessage() {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 创建连接成功消息
     * 
     * @param userId 用户ID
     * @param username 用户名
     * @return WebSocket消息
     */
    public static WebSocketMessage connectSuccess(Long userId, String username) {
        WebSocketMessage msg = new WebSocketMessage();
        msg.setType("connect_success");
        msg.setSenderId(userId);
        msg.setSenderName(username);
        msg.setData("WebSocket连接成功");
        return msg;
    }

    /**
     * 创建聊天消息
     * 
     * @param message 消息实体
     * @return WebSocket消息
     */
    public static WebSocketMessage chatMessage(Message message) {
        WebSocketMessage msg = new WebSocketMessage();
        msg.setType("chat_message");
        msg.setSenderId(message.getSenderId());
        msg.setGroupId(message.getGroupId());
        msg.setReceiverId(message.getReceiverId());
        msg.setData(message);
        return msg;
    }

    /**
     * 创建撤回消息通知
     * 
     * @param msgId 消息ID
     * @param operatorId 操作者ID
     * @param groupId 群组ID
     * @return WebSocket消息
     */
    public static WebSocketMessage recallMessage(String msgId, Long operatorId, Long groupId) {
        WebSocketMessage msg = new WebSocketMessage();
        msg.setType("recall_message");
        msg.setSenderId(operatorId);
        msg.setGroupId(groupId);
        
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("msgId", msgId);
        data.put("operatorId", operatorId);
        msg.setData(data);
        
        return msg;
    }

    /**
     * 创建用户状态消息
     * 
     * @param userId 用户ID
     * @param online 是否在线
     * @return WebSocket消息
     */
    public static WebSocketMessage userStatus(Long userId, boolean online) {
        WebSocketMessage msg = new WebSocketMessage();
        msg.setType("user_status");
        msg.setSenderId(userId);
        
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("userId", userId);
        data.put("online", online);
        msg.setData(data);
        
        return msg;
    }

    /**
     * 创建正在输入消息
     * 
     * @param userId 用户ID
     * @param groupId 群组ID(群聊)
     * @param receiverId 接收者ID(单聊)
     * @return WebSocket消息
     */
    public static WebSocketMessage typing(Long userId, Long groupId, Long receiverId) {
        WebSocketMessage msg = new WebSocketMessage();
        msg.setType("typing");
        msg.setSenderId(userId);
        msg.setGroupId(groupId);
        msg.setReceiverId(receiverId);
        return msg;
    }

    /**
     * 创建加入群组确认消息
     * 
     * @param groupId 群组ID
     * @return WebSocket消息
     */
    public static WebSocketMessage groupJoined(Long groupId) {
        WebSocketMessage msg = new WebSocketMessage();
        msg.setType("group_joined");
        msg.setGroupId(groupId);
        msg.setData("已成功加入群组" + groupId + "的实时频道");
        return msg;
    }

    /**
     * 创建心跳响应消息
     * 
     * @return WebSocket消息
     */
    public static WebSocketMessage pong() {
        WebSocketMessage msg = new WebSocketMessage();
        msg.setType("pong");
        msg.setData("pong");
        return msg;
    }

    /**
     * 创建错误消息
     * 
     * @param errorMessage 错误信息
     * @return WebSocket消息
     */
    public static WebSocketMessage error(String errorMessage) {
        WebSocketMessage msg = new WebSocketMessage();
        msg.setType("error");
        msg.setData(errorMessage);
        return msg;
    }
}
