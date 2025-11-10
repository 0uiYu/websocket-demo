
package com.lhy.websocketdemo.handle;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("✅ Client connected: " + session.getId());
        session.sendMessage(new TextMessage("{\"type\":\"info\",\"message\":\"Connected to server!\"}"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("📩 Received from " + session.getId() + ": " + payload);

        // 广播给所有客户端
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage("{\"type\":\"chat\",\"text\":\"" + payload + "\"}"));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("❌ Client disconnected: " + session.getId());
    }

    // 👇 新增：定时推送消息（服务端主动）
    @Scheduled(fixedRate = 5000) // 每 5 秒执行一次
    public void sendHeartbeat() {
        String msg = "{\"type\":\"system\",\"message\":\"Server heartbeat at " + new Date() + "\"}";
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(msg));
                } catch (IOException e) {
                    System.err.println("⚠️ Failed to send heartbeat: " + e.getMessage());
                }
            }
        }
    }
}