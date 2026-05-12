package com.groupchat.dto;

import lombok.Data;

/**
 * 用户登录响应DTO
 * 
 * 说明:
 * 1. 用于返回登录成功后的数据
 * 2. 包含Token和用户信息
 * 
 * @author GroupChat Team
 */
@Data
public class LoginResponse {

    /**
     * 访问令牌(Access Token)
     * 用于后续请求的认证
     */
    private String accessToken;

    /**
     * 刷新令牌(Refresh Token)
     * 用于Token过期后获取新的Access Token
     */
    private String refreshToken;

    /**
     * Token类型
     * 固定为"Bearer"
     */
    private String tokenType;

    /**
     * Token过期时间(秒)
     */
    private Long expiresIn;

    /**
     * 用户信息
     */
    private UserInfo user;

    /**
     * 用户信息内部类
     */
    @Data
    public static class UserInfo {
        /**
         * 用户ID
         */
        private Long id;

        /**
         * 用户名
         */
        private String username;

        /**
         * 昵称
         */
        private String nickname;

        /**
         * 头像URL
         */
        private String avatar;
    }
}
