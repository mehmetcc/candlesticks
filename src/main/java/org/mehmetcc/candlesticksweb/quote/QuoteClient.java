package org.mehmetcc.candlesticksweb.quote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;

@Slf4j
public class QuoteClient extends TextWebSocketHandler {
    @Autowired
    private QuoteRepository repository;

    private final ObjectMapper mapper;

    public QuoteClient() {
        this.mapper = new ObjectMapper();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Client connection opened");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("Client connection closed: {}", status);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        //log.info("Client received: {}", message);
        var event = readJson(message.getPayload());
        event.getData().setDate(LocalDateTime.now());
        repository.save(event.getData());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable error) {
        log.error("Client transport error: {}", error.getMessage());
    }

    private QuoteEvent readJson(String payload) {
        QuoteEvent json = null;
        try {
            json = mapper.readValue(payload, QuoteEvent.class);
        } catch (JsonProcessingException error) {
            log.error("Client transport error: {}", error.getMessage());
        }
        return json;
    }
}
