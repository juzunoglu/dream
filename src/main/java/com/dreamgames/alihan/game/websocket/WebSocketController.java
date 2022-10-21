package com.dreamgames.alihan.game.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
@Slf4j
public class WebSocketController {

    @Autowired
    private final WebSocketService webSocketService;

    public WebSocketController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @MessageMapping("/message/{toUser}")
    public Boolean sendMessage(
            Principal principal,
            @Header String authKey,
            @DestinationVariable String toUser,
            @RequestBody WebSocketRequestMessage message
    ) {
        log.info("Send message : {} from user {} to user {}", message.messageContent, principal.getName(), toUser);
        try {
            webSocketService.notifyUser(toUser, message.getMessageContent());
        } catch (JsonProcessingException exception) {
            log.error("An error occurred while notifying: {}", exception.toString());
        }
        return Boolean.TRUE;
    }

    @Data
    public static class WebSocketRequestMessage {
        private String messageContent;
    }

}
