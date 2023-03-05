package org.mehmetcc.candlesticksweb.quote;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Configuration
public class QuoteWebSocketConfiguration {
    @Value("${application.websocket.endpoints.quotes}")
    private String endpoint;

    @Bean
    public WebSocketConnectionManager quoteWebSocketConnectionManager() {
        WebSocketConnectionManager manager = new WebSocketConnectionManager(
                quoteWebSocketClient(),
                quoteClient(),
                endpoint
        );
        manager.setAutoStartup(true);
        return manager;
    }

    @Bean
    public WebSocketClient quoteWebSocketClient() {
        return new StandardWebSocketClient();
    }

    @Bean
    public WebSocketHandler quoteClient() {
        return new QuoteClient();
    }

}
