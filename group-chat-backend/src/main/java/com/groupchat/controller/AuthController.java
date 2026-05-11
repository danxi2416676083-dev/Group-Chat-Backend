package com.groupchat.controller;

import com.groupchat.common.Result;
import com.groupchat.dto.LoginRequest;
import com.groupchat.dto.LoginResponse;
import com.groupchat.dto.RegisterRequest;
import com.groupchat.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 
 * 说明:
 * 1. 处理用户登录、注册、Token刷新等认证相关请求
 * 2. 所有接口都是公开的，不需要JWT认证
 * 
 * 接口列表:
 * - POST /api/auth/login: 用户登录
 * - POST /api/auth/register: 用户注册
 * - POST /api/auth/refresh: 刷新Token
 * 
 * @author GroupChat Team
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    /**
     * 认证服务
     */
    private final AuthService authService;

    /**
     * 用户登录
     * 
     * 请求示例:
     * POST /api/auth/login
     * {
     *   "username": "user1",
     *   "password": "123456"
     * }
     * 
     * 响应示例:
     * {
     *   "code": 200,
     *   "message": "success",
     *   "data": {
     *     "accessToken": "eyJhbGciOiJIUzI1NiIs...",
     *     "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
     *     "tokenType": "Bearer",
     *     "expiresIn": 7200,
     *     "user": {
     *       "id": 1,
     *       "username": "user1",
     *       "nickname": "张三",
     *       "avatar": "https://..."
     *     }
     *   }
     * }
     * 
     * @param request 登录请求
     * @return 登录响应
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return Result.success("登录成功", response);
    }

    /**
     * 用户注册
     * 
     * 请求示例:
     * POST /api/auth/register
     * {
     *   "username": "newuser",
     *   "password": "123456",
     *   "confirmPassword": "123456",
     *   "nickname": "新用户",
     *   "email": "newuser@example.com"
     * }
     * 
     * @param request 注册请求
     * @return 登录响应(自动登录)
     */
    @PostMapping("/register")
    public Result<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        LoginResponse response = authService.register(request);
        return Result.success("注册成功", response);
    }

    /**
     * 刷新Token
     * 
     * 请求示例:
     * POST /api/auth/refresh
     * Header: Authorization: Bearer {refresh_token}
     * 
     * @param refreshToken 刷新令牌(从Header中提取)
     * @return 新的登录响应
     */
    @PostMapping("/refresh")
    public Result<LoginResponse> refreshToken(
            @RequestHeader("Authorization") String refreshToken) {
        // 提取Token(去掉Bearer前缀)
        if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }
        
        LoginResponse response = authService.refreshToken(refreshToken);
        return Result.success("Token刷新成功", response);
    }
}
