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
    public WebSocketConnectionManager instrumentWebSocketConnectionManager() {
        WebSocketConnectionManager manager = new WebSocketConnectionManager(
                instrumentWebSocketClient(),
                instrumentClient(),
                "ws://partner-service:8032/instruments" // TODO configure
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
