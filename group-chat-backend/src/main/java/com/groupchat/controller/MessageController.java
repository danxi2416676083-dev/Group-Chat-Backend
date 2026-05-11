package com.groupchat.controller;

import com.groupchat.common.Result;
import com.groupchat.dto.MessageResponse;
import com.groupchat.dto.SendMessageRequest;
import com.groupchat.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息控制器
 * 
 * 说明:
 * 1. 处理消息发送、查询、撤回等操作
 * 2. 支持群聊和单聊消息
 * 3. 所有接口都需要JWT认证
 * 
 * 接口列表:
 * - POST /api/messages: 发送消息
 * - GET /api/messages/group/{groupId}: 获取群组历史消息
 * - GET /api/messages/private/{friendId}: 获取单聊消息
 * - POST /api/messages/{msgId}/recall: 撤回消息
 * - GET /api/messages/{msgId}: 获取消息详情
 * 
 * @author GroupChat Team
 */
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    /**
     * 消息服务
     */
    private final MessageService messageService;

    /**
     * 发送消息
     * 
     * 请求示例:
     * POST /api/messages
     * {
     *   "msgId": "msg-uuid-123",
     *   "sessionType": 1,
     *   "groupId": 1,
     *   "msgType": 0,
     *   "content": "大家好！"
     * }
     * 
     * @param userDetails 当前登录用户
     * @param request 发送请求
     * @return 消息信息
     */
    @PostMapping
    public Result<MessageResponse> sendMessage(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody SendMessageRequest request) {
        Long senderId = Long.valueOf(userDetails.getUsername());
        MessageResponse response = messageService.sendMessage(senderId, request);
        return Result.success("发送成功", response);
    }

    /**
     * 获取群组历史消息
     * 
     * 请求示例:
     * GET /api/messages/group/1?before=2024-01-01T00:00:00&limit=20
     * 
     * @param userDetails 当前登录用户
     * @param groupId 群组ID
     * @param beforeTime 在此时间之前(用于分页，可选)
     * @param limit 查询数量(默认20)
     * @return 消息列表
     */
    @GetMapping("/group/{groupId}")
    public Result<List<MessageResponse>> getGroupMessages(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long groupId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beforeTime,
            @RequestParam(defaultValue = "20") Integer limit) {
        Long userId = Long.valueOf(userDetails.getUsername());
        List<MessageResponse> messages = messageService.getGroupMessages(groupId, userId, beforeTime, limit);
        return Result.success(messages);
    }

    /**
     * 获取单聊消息
     * 
     * 请求示例:
     * GET /api/messages/private/2?limit=20
     * 
     * @param userDetails 当前登录用户
     * @param friendId 好友ID
     * @param limit 查询数量(默认20)
     * @return 消息列表
     */
    @GetMapping("/private/{friendId}")
    public Result<List<MessageResponse>> getPrivateMessages(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long friendId,
            @RequestParam(defaultValue = "20") Integer limit) {
        Long userId = Long.valueOf(userDetails.getUsername());
        List<MessageResponse> messages = messageService.getPrivateMessages(userId, friendId, limit);
        return Result.success(messages);
    }

    /**
     * 撤回消息
     * <p>
     * 请求示例:
     * POST /api/messages/msg-uuid-123/recall
     *
     * @param userDetails 当前登录用户
     * @param msgId       消息ID
     * @return 成功响应
     */
    @PostMapping("/{msgId}/recall")
    public Result<String> recallMessage(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String msgId) {
        Long userId = Long.valueOf(userDetails.getUsername());
        messageService.recallMessage(msgId, userId);
        return Result.success("撤回成功");
    }

    /**
     * 获取消息详情
     * 
     * 请求示例:
     * GET /api/messages/msg-uuid-123
     * 
     * @param userDetails 当前登录用户
     * @param msgId 消息ID
     * @return 消息详情
     */
    @GetMapping("/{msgId}")
    public Result<MessageResponse> getMessageDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String msgId) {
        Long userId = Long.valueOf(userDetails.getUsername());
        MessageResponse response = messageService.getMessageDetail(msgId, userId);
        return Result.success(response);
    }
}
