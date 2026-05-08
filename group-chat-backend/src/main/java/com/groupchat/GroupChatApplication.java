package com.groupchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 群聊系统后端应用程序入口类
 * 
 * 说明:
 * 1. 使用@SpringBootApplication注解标记为Spring Boot应用
 * 2. 包含@ComponentScan、@EnableAutoConfiguration、@Configuration三个注解的功能
 * 3. 自动扫描当前包及其子包下的所有Spring组件
 * 
 * 技术栈: Spring Boot 3.x + JDK 21/24
 * 
 * @author GroupChat Team
 * @version 1.0.0
 */
@SpringBootApplication
public class GroupChatApplication {

    /**
     * 应用程序入口方法
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 启动Spring Boot应用0
        // SpringApplication.run会执行以下操作:
        // 1. 创建Spring应用上下文
        // 2. 加载所有配置类
        // 3. 启动内嵌的Tomcat服务器
        // 4. 初始化所有Spring Bean
        SpringApplication.run(GroupChatApplication.class, args);
        
        // 打印启动成功信息
        System.out.println("========================================");
        System.out.println("  群聊系统后端服务启动成功!");
        System.out.println("  访问地址: http://localhost:8080/api");
        System.out.println("  WebSocket: ws://localhost:8080/api/ws/chat");
        System.out.println("========================================");
    }
}
