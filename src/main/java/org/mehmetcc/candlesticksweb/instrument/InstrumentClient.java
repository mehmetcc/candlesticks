package org.mehmetcc.candlesticksweb.instrument;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
public class InstrumentClient extends TextWebSocketHandler {
    @Autowired
    private InstrumentService service;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        service.match(message.getPayload())
                .fold(
                        error -> {
                            log.error(error);
                            return error;
                        },
                        success -> {
                            log.debug("Instrument ops: " + success.toString());
                            return success;
                        }
                );
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable error) {
        log.error("Client transport error: {}", error.getMessage());
    }
}
