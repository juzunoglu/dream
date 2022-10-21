package com.dreamgames.alihan.game.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebSocketService {

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyUser(String userId, String message) throws JsonProcessingException {
        String json = (new ObjectMapper()).writeValueAsString(
                WebSocketResponseMessage
                        .builder()
                        .content(message)
                        .build()
        );
        messagingTemplate.convertAndSendToUser(userId, "/topic/messages", json);
    }

    public void publish(String message) { // todo
        redisTemplate.convertAndSend("/topic/messages", message);
    }
    public void publish2(String message) { //todo
        messagingTemplate.convertAndSend("/topic/messages", message);
    }

    @Data
    @Builder
    public static class WebSocketResponseMessage {
        private String content;
    }
}
