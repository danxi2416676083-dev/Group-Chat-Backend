package com.groupchat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户登录请求DTO
 * 
 * 说明:
 * 1. 用于接收用户登录请求参数
 * 2. 使用JSR-303注解进行参数校验
 * 
 * @author GroupChat Team
 */
@Data
public class LoginRequest {

    /**
     * 用户名
     * @NotBlank 表示不能为空字符串
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     * @NotBlank 表示不能为空字符串
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}
