package org.mehmetcc.candlesticksweb.candlestick;

import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CandlestickServiceTest {
    @Mock
    private CandlestickRepository repository;

    @InjectMocks
    private CandlestickService service;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(service, "candlestickLimit", 30);
    }

    @Test
    void shouldFillIn() {
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
        // Stubbing
        when(repository.findAllByIsin(any(String.class))).thenReturn(Collections.singletonList(candlestick));
        // Interaction
        var result = service.retrieveCandlesticks("isbnk");
        // Assertion
        assertThat(result).isInstanceOf(Either.Right.class);
        assertThat(result.get()).isNotEmpty()
                .hasSize(30)
                .element(0)
                .isEqualTo(candlestick);
    }

    @Test
    void shouldRaiseErrorMessage() {
        // Stubbing
        when(repository.findAllByIsin(any(String.class))).thenReturn(Collections.emptyList());
        // Interaction
        var result = service.retrieveCandlesticks("isbnk");
        // Assertion
        assertThat(result).isInstanceOf(Either.Left.class);
        assertThat(result.getLeft().message()).isEqualTo("No entries found!");
    }
}