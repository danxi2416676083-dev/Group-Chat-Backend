package com.groupchat.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket配置类
 * 
 * 说明:
 * 1. 启用WebSocket支持
 * 2. 注册WebSocket处理器
 * 3. 配置WebSocket端点
 * 
 * @author GroupChat Team
 */
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    /**
     * WebSocket服务器处理器
     */
    private final WebSocketServer webSocketServer;

    /**
     * 注册WebSocket处理器
     * 
     * @param registry WebSocket处理器注册器
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册WebSocket端点
        // /ws/chat 是WebSocket连接路径
        // setAllowedOrigins("*") 允许所有来源(生产环境应限制具体域名)
        registry.addHandler(webSocketServer, "/ws/chat")
                .setAllowedOrigins("*");
        
        // 如果使用SockJS(兼容不支持WebSocket的浏览器)
        // registry.addHandler(webSocketServer, "/ws/chat")
        //         .setAllowedOrigins("*")
        //         .withSockJS();
    }
}
