package org.mehmetcc.candlesticksweb.instrument;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Configuration
public class InstrumentWebSocketConfiguration {
    @Value("${application.websocket.endpoints.instruments}")
    private String endpoint;

    @Bean
    public WebSocketConnectionManager instrumentWebSocketConnectionManager() {
        WebSocketConnectionManager manager = new WebSocketConnectionManager(
                instrumentWebSocketClient(),
                instrumentClient(),
                endpoint
        );
        manager.setAutoStartup(true);
        return manager;
    }

    @Bean
    public WebSocketClient instrumentWebSocketClient() {
        return new StandardWebSocketClient();
    }

    @Bean
    public WebSocketHandler instrumentClient() {
        return new InstrumentClient();
    }
}
