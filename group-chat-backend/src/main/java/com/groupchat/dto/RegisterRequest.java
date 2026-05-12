package com.groupchat.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户注册请求DTO
 * 
 * 说明:
 * 1. 用于接收用户注册请求参数
 * 2. 包含详细的参数校验规则
 * 
 * @author GroupChat Team
 */
@Data
public class RegisterRequest {

    /**
     * 用户名
     * 规则: 4-20位字母、数字、下划线
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度必须在4-20位之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字、下划线")
    private String username;

    /**
     * 密码
     * 规则: 6-20位任意字符
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String password;

    /**
     * 确认密码
     * 用于校验两次输入是否一致
     */
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    /**
     * 昵称
     * 规则: 2-20位任意字符
     */
    @NotBlank(message = "昵称不能为空")
    @Size(min = 2, max = 20, message = "昵称长度必须在2-20位之间")
    private String nickname;

    /**
     * 邮箱(可选)
     * @Email 自动校验邮箱格式
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 手机号(可选)
     * 规则: 中国大陆手机号格式
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
}
