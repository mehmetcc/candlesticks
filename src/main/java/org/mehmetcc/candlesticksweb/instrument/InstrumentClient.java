package org.mehmetcc.candlesticksweb.instrument;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
public class InstrumentClient extends TextWebSocketHandler {
    @Autowired
    private InstrumentRepository repository;

    private final ObjectMapper mapper;

    public InstrumentClient() {
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
        matchEventType(event);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable error) {
        log.error("Client transport error: {}", error.getMessage());
    }

    private InstrumentEvent readJson(String payload) {
        InstrumentEvent json = null;
        try {
            json = mapper.readValue(payload, InstrumentEvent.class);
        } catch (JsonProcessingException error) {
            log.error("Client transport error: {}", error.getMessage());
        }
        return json;
    }

    private void matchEventType(InstrumentEvent event) {
        if (event.getType() == InstrumentEventType.DELETE) {
            repository.deleteById(event.getData().getIsin());
            log.info("Deleted {}", event.getData());
        }
        var persisted = repository.save(event.getData());
        log.info("Saved {}", event.getData());
    }
}
