package com.groupchat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 群组信息响应DTO
 * 
 * 说明:
 * 1. 用于返回群组的详细信息
 * 2. 包含群组基本信息、成员数、当前用户角色等
 * 
 * @author GroupChat Team
 */
@Data
public class GroupResponse {

    /**
     * 群组ID
     */
    private Long id;

    /**
     * 群组名称
     */
    private String name;

    /**
     * 群组描述
     */
    private String description;

    /**
     * 群组头像
     */
    private String avatar;

    /**
     * 群主ID
     */
    private Long ownerId;

    /**
     * 群主昵称
     */
    private String ownerNickname;

    /**
     * 群组类型
     * 0-普通群, 1-企业群, 2-临时群
     */
    private Integer type;

    /**
     * 最大成员数
     */
    private Integer maxMembers;

    /**
     * 当前成员数
     */
    private Integer memberCount;

    /**
     * 入群方式
     * 0-自由加入, 1-需要验证, 2-禁止加入, 3-邀请加入
     */
    private Integer joinType;

    /**
     * 群组公告
     */
    private String announcement;

    /**
     * 当前用户在群中的角色
     * 0-非成员, 1-成员, 2-管理员, 3-群主
     */
    private Integer myRole;

    /**
     * 当前用户在群中的昵称
     */
    private String myGroupNickname;

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
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
