package com.groupchat.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 
 * 说明:
 * 1. 对应数据库表 gc_user
 * 2. 使用MyBatis-Plus注解进行ORM映射
 * 3. 包含用户基本信息、登录信息、状态等字段
 * 
 * @author GroupChat Team
 */
@Data
@TableName("gc_user")
public class User {

    /**
     * 用户ID
     * 使用@TableId标记为主键
     * IdType.AUTO表示数据库自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     * 用于登录，唯一
     */
    private String username;

    /**
     * 密码
     * 使用BCrypt加密存储
     */
    private String password;

    /**
     * 昵称
     * 显示名称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别
     * 0-未知, 1-男, 2-女
     */
    private Integer gender;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 状态
     * 0-禁用, 1-正常
     */
    private Integer status;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

    /**
     * 创建时间
     * @TableField(fill = FieldFill.INSERT) 表示插入时自动填充
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     * @TableField(fill = FieldFill.INSERT_UPDATE) 表示插入和更新时自动填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除标志
     * 0-未删除, 1-已删除
     * @TableLogic 标记为逻辑删除字段
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
