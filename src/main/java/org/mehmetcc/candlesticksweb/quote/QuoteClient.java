package org.mehmetcc.candlesticksweb.quote;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
public class QuoteClient extends TextWebSocketHandler {
    @Autowired
    private QuoteService service;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        service.persist(message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable error) {
        log.error("Client transport error: {}", error.getMessage());
    }
}