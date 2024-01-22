package com.smart.message.websocket.server;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author shizhongming
 * 2023/1/21 20:14
 * @since 3.0.0
 */
@Slf4j
@ServerEndpoint("/websocket/{userId}")
public class WebSocket {

    private static final ConcurrentMap<Long, Session> WEBSOCKET_SESSION_POOL = new ConcurrentHashMap<>();


    /**
     * 建立新的连接
     * @param session SESSION
     * @param userId 用户ID
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") Long userId) {
        try {
            WEBSOCKET_SESSION_POOL.put(userId, session);
            log.info("【websocket消息】有新的连接，用户ID：{}，总连接数：{}", userId, WEBSOCKET_SESSION_POOL.size());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 断开连接
     * @param userId 用户ID
     */
    @OnClose
    public void onClose(@PathParam("userId") Long userId) {
        try {
            WEBSOCKET_SESSION_POOL.remove(userId);
            log.info("【websocket消息】断开连接，用户ID：{}，总连接数：{}", userId, WEBSOCKET_SESSION_POOL.size());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 向所有用户发哦是哪个消息
     * @param message 消息
     */
    public void pushMessage(String message) {
        WEBSOCKET_SESSION_POOL.values().stream()
                .filter(Session::isOpen)
                .forEach(session -> {
                    try {
                        session.getAsyncRemote().sendText(message);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                });
    }

    /**
     * 发送消息
     * @param userId 用户ID
     * @param message 消息
     */
    public void pushMessage(Long userId, String message) {
        Session session = WEBSOCKET_SESSION_POOL.get(userId);
        if (session == null) {
            log.warn("用户未连接，发送消息失败，用户ID：{}", userId);
            return;
        }
        if (!session.isOpen()) {
            log.warn("用户连接已关闭，发送消息失败，用户ID：{}", userId);
        }
        try {
            log.info("【websocket消息】 单点消息:" + message);
            session.getAsyncRemote().sendText(message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
