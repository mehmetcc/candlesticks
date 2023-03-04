package org.mehmetcc.candlesticksweb.candlestick;

import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
class CandlestickControllerTest {
    @Mock
    private CandlestickService service;

    @InjectMocks
    private CandlestickController controller;

    @Test
    void shouldReturnList() {
        // Data prep
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
        // Stubbing
        when(service.retrieveCandlesticks(any(String.class))).thenReturn(Either.right(candlesticks));
        // Interaction
        var result = controller.all("isbnk");
        // Assertion
        assertThat(result.getBody())
                .isInstanceOf(List.class);
    }

    @Test
    void shouldReturnErrorMessage() {
        // Stubbing
        when(service.retrieveCandlesticks(any(String.class)))
                .thenReturn(Either.left(new CandlestickService
                        .CandlestickServiceException("No entries found!", HttpStatus.BAD_REQUEST)));
        // Interaction
        var result = controller.all("isbnk");
        // Assertion
        assertThat(result.getBody()).isNotNull().isEqualTo("No entries found!");
        assertThat(result.getStatusCode()).isNotNull().isEqualTo(HttpStatus.BAD_REQUEST);
    }
}