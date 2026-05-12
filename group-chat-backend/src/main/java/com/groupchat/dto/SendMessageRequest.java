package com.groupchat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

/**
 * 发送消息请求DTO
 * 
 * 说明:
 * 1. 用于接收发送消息的请求参数
 * 2. 支持文本、图片、语音、视频、文件等多种消息类型
 * 
 * @author GroupChat Team
 */
@Data
public class SendMessageRequest {

    /**
     * 消息ID(UUID)
     * 由客户端生成，用于消息去重
     */
    @NotBlank(message = "消息ID不能为空")
    private String msgId;

    /**
     * 会话类型
     * 0-单聊, 1-群聊
     */
    @NotNull(message = "会话类型不能为空")
    private Integer sessionType;

    /**
     * 群组ID
     * 群聊时必填
     */
    private Long groupId;

    /**
     * 接收者ID
     * 单聊时必填
     */
    private Long receiverId;

    /**
     * 消息类型
     * 0-文本, 1-图片, 2-语音, 3-视频, 4-文件, 5-系统消息
     */
    @NotNull(message = "消息类型不能为空")
    private Integer msgType;

    /**
     * 消息内容
     * 文本消息直接存储文本
     * 多媒体消息存储描述或URL
     */
    @NotBlank(message = "消息内容不能为空")
    private String content;

    /**
     * 额外数据(JSON格式)
     * 图片URL、文件信息、语音时长等
     */
    private String extraData;

    /**
     * 引用消息ID
     * 回复某条消息时使用
     */
    private String replyToMsgId;

    /**
     * @的用户ID列表
     */
    private List<Long> atUserIds;
}
