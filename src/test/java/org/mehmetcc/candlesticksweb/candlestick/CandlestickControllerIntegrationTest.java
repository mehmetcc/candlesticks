package org.mehmetcc.candlesticksweb.candlestick;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class CandlestickControllerIntegrationTest {
    @Autowired
    private CandlestickController controller;

    @Autowired
    private CandlestickRepository repository;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        var now = LocalDateTime.now().withSecond(0);
        var candlestick = Candlestick.builder()
                .id(1)
                .openingTimestamp(now)
                .isin("isbnk")
                .openingPrice(1.0)
                .highestPrice(5.9)
                .lowestPrice(1.0)
                .closingPrice(5.9)
                .closingTimestamp(now.plusSeconds(30))
                .build();
        var candlesticks = new ArrayList<Candlestick>();
        candlesticks.add(candlestick);
        repository.saveAll(candlesticks);
    }

    @Test
    void retrieveByIsin() throws Exception {
        mvc.perform(get("/candlesticks?isin=isbnk")).andExpect(status().isOk());
    }
}