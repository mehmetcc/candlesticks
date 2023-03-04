package org.mehmetcc.candlesticksweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories
@EnableAutoConfiguration
public class CandlesticksWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(CandlesticksWebApplication.class, args);
    }
}
