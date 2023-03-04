package org.mehmetcc.candlesticksweb.instrument;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Configuration
public class InstrumentWebSocketConfiguration {
    @Bean
    public WebSocketConnectionManager webSocketConnectionManager() {
        WebSocketConnectionManager manager = new WebSocketConnectionManager(
                webSocketClient(),
                instrumentClient(),
                "ws://partner-service:8032/instruments"
        );
        manager.setAutoStartup(true);
        return manager;
    }

    @Bean
    public WebSocketClient webSocketClient() {
        return new StandardWebSocketClient();
    }

    @Bean
    public WebSocketHandler instrumentClient() {
        return new InstrumentClient();
    }
}
