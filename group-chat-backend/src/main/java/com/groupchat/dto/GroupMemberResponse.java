package com.groupchat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 群组成员响应DTO
 * 
 * 说明:
 * 1. 用于返回群组成员的详细信息
 * 2. 包含用户基本信息和在群中的角色
 * 
 * @author GroupChat Team
 */
@Data
public class GroupMemberResponse {

    /**
     * 成员关系ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 群昵称
     */
    private String groupNickname;

    /**
     * 成员角色
     * 0-成员, 1-管理员, 2-群主
     */
    private Integer role;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 入群时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinedAt;

    /**
     * 最后发言时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastSpeakTime;

    /**
     * 是否禁言
     */
    private Boolean muted;

    /**
     * 禁言截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime muteEndTime;

    /**
     * 在线状态
     */
    private Boolean online;
}
