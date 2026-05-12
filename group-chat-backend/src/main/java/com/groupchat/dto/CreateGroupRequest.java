package com.groupchat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建群组请求DTO
 * 
 * 说明:
 * 1. 用于接收创建群组的请求参数
 * 2. 包含群组基本信息和初始成员
 * 
 * @author GroupChat Team
 */
@Data
public class CreateGroupRequest {

    /**
     * 群组名称
     */
    @NotBlank(message = "群组名称不能为空")
    @Size(min = 1, max = 50, message = "群组名称长度必须在1-50位之间")
    private String name;

    /**
     * 群组描述(可选)
     */
    @Size(max = 200, message = "群组描述长度不能超过200位")
    private String description;

    /**
     * 群组头像URL(可选)
     */
    private String avatar;

    /**
     * 群组类型
     * 0-普通群, 1-企业群, 2-临时群
     */
    private Integer type = 0;

    /**
     * 最大成员数(可选，默认500)
     */
    private Integer maxMembers = 500;

    /**
     * 入群方式
     * 0-自由加入, 1-需要验证, 2-禁止加入, 3-邀请加入
     */
    private Integer joinType = 0;

    /**
     * 初始成员ID列表(可选)
     * 创建群组时同时邀请的成员
     */
    private java.util.List<Long> initialMembers;
}
