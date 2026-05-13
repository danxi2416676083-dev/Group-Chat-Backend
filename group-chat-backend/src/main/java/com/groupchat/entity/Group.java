package com.groupchat.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 群组实体类
 * 
 * 说明:
 * 1. 对应数据库表 gc_group
 * 2. 存储群组的基本信息、配置、状态等
 * 3. 群主通过owner_id关联User表
 * 
 * @author GroupChat Team
 */
@Data
@TableName("gc_group")
public class Group {

    /**
     * 群组ID
     */
    @TableId(type = IdType.AUTO)
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
     * 群组头像URL
     */
    private String avatar;

    /**
     * 群主ID
     * 关联gc_user表的id字段
     */
    private Long ownerId;

    /**
     * 群组类型
     * 0-普通群, 1-企业群, 2-临时群
     */
    private Integer type;

    /**
     * 最大成员数
     * 默认500
     */
    private Integer maxMembers;

    /**
     * 当前成员数
     */
    private Integer memberCount;

    /**
     * 群组公告
     */
    private String announcement;

    /**
     * 入群方式
     * 0-自由加入, 1-需要验证, 2-禁止加入, 3-邀请加入
     */
    private Integer joinType;

    /**
     * 状态
     * 0-解散, 1-正常
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

    /**
     * 逻辑删除标志
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
