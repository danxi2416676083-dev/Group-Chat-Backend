package com.groupchat.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 群组成员实体类
 * 
 * 说明:
 * 1. 对应数据库表 gc_group_member
 * 2. 存储用户与群组的关联关系
 * 3. 包含成员角色、状态、禁言等信息
 * 
 * @author GroupChat Team
 */
@Data
@TableName("gc_group_member")
public class GroupMember {

    /**
     * 成员关系ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 群组ID
     */
    private Long groupId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 成员角色
     * 0-成员, 1-管理员, 2-群主
     */
    private Integer role;

    /**
     * 群昵称
     * 在群内的显示名称
     */
    private String groupNickname;

    /**
     * 入群时间
     */
    private LocalDateTime joinedAt;

    /**
     * 最后发言时间
     */
    private LocalDateTime lastSpeakTime;

    /**
     * 是否禁言
     * 0-否, 1-是
     */
    private Integer muted;

    /**
     * 禁言截止时间
     */
    private LocalDateTime muteEndTime;

    /**
     * 状态
     * 0-已退出, 1-正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
