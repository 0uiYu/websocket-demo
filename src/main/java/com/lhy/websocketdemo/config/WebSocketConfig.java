// src/main/java/com/example/websocketdemo/WebSocketConfig.java
package com.lhy.websocketdemo.config;

import com.lhy.websocketdemo.handle.ChatWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;

    public WebSocketConfig(ChatWebSocketHandler chatWebSocketHandler) {
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册处理器，允许跨域（开发用）
        registry.addHandler(chatWebSocketHandler, "/ws")
                .setAllowedOrigins("*"); // 生产环境应限制域名
    }
}