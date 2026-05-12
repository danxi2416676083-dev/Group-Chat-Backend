package com.groupchat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息响应DTO
 * 
 * 说明:
 * 1. 用于返回消息的详细信息
 * 2. 包含发送者信息、消息内容、状态等
 * 
 * @author GroupChat Team
 */
@Data
public class MessageResponse {

    /**
     * 消息UUID
     */
    private String msgId;

    /**
     * 会话类型
     * 0-单聊, 1-群聊
     */
    private Integer sessionType;

    /**
     * 群组ID
     */
    private Long groupId;

    /**
     * 发送者ID
     */
    private Long senderId;

    /**
     * 发送者昵称
     */
    private String senderNickname;

    /**
     * 发送者头像
     */
    private String senderAvatar;

    /**
     * 发送者在群中的昵称(群聊时有效)
     */
    private String senderGroupNickname;

    /**
     * 接收者ID
     */
    private Long receiverId;

    /**
     * 消息类型
     * 0-文本, 1-图片, 2-语音, 3-视频, 4-文件, 5-系统消息
     */
    private Integer msgType;

    /**
     * 消息类型名称
     */
    private String msgTypeName;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 额外数据
     */
    private String extraData;

    /**
     * 引用消息ID
     */
    private String replyToMsgId;

    /**
     * 引用消息内容预览
     */
    private String replyToMsgPreview;

    /**
     * @的用户ID列表
     */
    private List<Long> atUserIds;

    /**
     * 是否已撤回
     */
    private Boolean recalled;

    /**
     * 撤回时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recalledAt;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

    /**
     * 是否自己发送的消息
     */
    private Boolean isSelf;
}
