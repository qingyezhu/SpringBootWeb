package com.wangzhu.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wang.zhu on 2021-01-11 14:50.
 **/
@Slf4j
@Component
@ServerEndpoint(("/api/websocket/{sid}"))
public class WebSocketServer {

    private static AtomicInteger online = new AtomicInteger();
    private static ConcurrentMap<String, Session> sessionConcurrentMap = new ConcurrentHashMap<>();

    private Session session;
    private String sid;

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        this.sid = sid;
        this.online.incrementAndGet();
        this.sessionConcurrentMap.put(this.sid, session);
        log.info("建立连接 sid|{}", this.sid);
    }

    @OnClose
    public void onClose() {
        this.online.decrementAndGet();
        this.sessionConcurrentMap.remove(this.sid);
        log.info("断开连接 sid|{}", this.sid);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到消息 sid|{}|message|{}", this.sid, message);
        sendMessage(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("出错了 sid|{}", this.sid, throwable);
    }

    private void sendMessage(String text) {
        for (final Session session : this.sessionConcurrentMap.values()) {
            sendMessage(session, text);
        }
    }

    private void sendMessage(final Session session, final String text) {
        try {
            session.getBasicRemote().sendText(text + ", 当前在线人数：" + online.get());
        } catch (Exception e) {
            log.error("发生异常 text|{}", text, e);
        }
    }
}
