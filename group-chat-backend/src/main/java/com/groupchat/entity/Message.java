package com.groupchat.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 消息实体类
 * 
 * 说明:
 * 1. 对应数据库表 gc_message
 * 2. 存储所有聊天消息(单聊和群聊)
 * 3. 使用msg_id作为全局唯一标识(UUID)
 * 
 * @author GroupChat Team
 */
@Data
@TableName("gc_message")
public class Message {

    /**
     * 消息自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 消息UUID
     * 全局唯一标识，用于客户端消息去重和引用
     */
    private String msgId;

    /**
     * 会话类型
     * 0-单聊, 1-群聊
     */
    private Integer sessionType;

    /**
     * 群组ID
     * 群聊时有效
     */
    private Long groupId;

    /**
     * 发送者ID
     */
    private Long senderId;

    /**
     * 接收者ID
     * 单聊时有效
     */
    private Long receiverId;

    /**
     * 消息类型
     * 0-文本, 1-图片, 2-语音, 3-视频, 4-文件, 5-系统消息
     */
    private Integer msgType;

    /**
     * 消息内容
     * 文本消息直接存储文本内容
     * 多媒体消息存储描述或URL
     */
    private String content;

    /**
     * 额外数据(JSON格式)
     * 存储图片URL、文件信息、语音时长等
     */
    private String extraData;

    /**
     * 引用消息ID
     * 回复某条消息时使用
     */
    private String replyToMsgId;

    /**
     * @的用户ID列表(JSON数组)
     */
    private String atUserIds;

    /**
     * 是否已撤回
     * 0-否, 1-是
     */
    private Integer recalled;

    /**
     * 撤回时间
     */
    private LocalDateTime recalledAt;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
