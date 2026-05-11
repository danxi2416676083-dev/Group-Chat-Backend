package com.groupchat.controller;

import com.groupchat.common.Result;
import com.groupchat.entity.User;
import com.groupchat.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 * 
 * 说明:
 * 1. 处理用户信息查询等操作
 * 2. 所有接口都需要JWT认证
 * 
 * 接口列表:
 * - GET /api/users/me: 获取当前登录用户信息
 * 
 * @author GroupChat Team
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    /**
     * 认证服务
     */
    private final AuthService authService;

    /**
     * 获取当前登录用户信息
     * 
     * 请求示例:
     * GET /api/users/me
     * Header: Authorization: Bearer {access_token}
     * 
     * 响应示例:
     * {
     *   "code": 200,
     *   "message": "success",
     *   "data": {
     *     "id": 1,
     *     "username": "user1",
     *     "nickname": "张三",
     *     "avatar": "https://...",
     *     "email": "user1@example.com",
     *     "phone": "13800138001",
     *     "gender": 1,
     *     "signature": "大家好"
     *   }
     * }
     * 
     * @param userDetails 当前登录用户
     * @return 用户信息
     */
    @GetMapping("/me")
    public Result<User> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.valueOf(userDetails.getUsername());
        User user = authService.getCurrentUser(userId);
        // 清除敏感信息
        user.setPassword(null);
        return Result.success(user);
    }
}
