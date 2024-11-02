package com.jabguru.websocketdemo.plain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class ChatMessageHandler implements WebSocketHandler {
    ObjectMapper objectMapper = new ObjectMapper();
    List<String> messages = new ArrayList<>();
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("connection established");
        sessions.add(session);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(messages)));

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("message arrived {}", message);
        messages.add((String) message.getPayload());
        String messageString = objectMapper.writeValueAsString(messages);
        session.sendMessage(new TextMessage(messageString));
    }

    public void sendMessage(String message) throws IOException {
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    messages.add(message);
                    String messageString = objectMapper.writeValueAsString(messages);
                    session.sendMessage(new TextMessage(messageString));
                }
            }
        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("transport error");

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessions.remove(session);
        log.info("connection closed");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
