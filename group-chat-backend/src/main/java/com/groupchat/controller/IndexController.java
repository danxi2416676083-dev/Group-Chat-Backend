package com.groupchat.controller;

import com.groupchat.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * API入口控制器
 *
 * @author GroupChat Team
 */
@RestController
public class IndexController {

    /**
     * API根路径，用于浏览器直接访问或启动验证。
     *
     * @return API运行状态与常用入口
     */
    @GetMapping({"/", ""})
    public Result<Map<String, Object>> index() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("service", "Group Chat API");
        data.put("status", "running");
        data.put("health", "/api/actuator/health");
        data.put("login", "POST /api/auth/login");
        data.put("register", "POST /api/auth/register");
        data.put("websocket", "ws://localhost:8080/api/ws/chat?token={jwt_token}");
        return Result.success("群聊系统后端服务运行中", data);
    }
}
