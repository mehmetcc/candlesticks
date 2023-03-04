package org.mehmetcc.candlesticksweb.candlestick;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mehmetcc.candlesticksweb.quote.Quote;
import org.mehmetcc.candlesticksweb.quote.QuoteRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CandlestickTaskTest {
    @Mock
    private CandlestickRepository candlestickRepository;

    @Mock
    private QuoteRepository quoteRepository;

    @InjectMocks
    private CandlestickTask task;


    @Test
    void shouldGroupByDifferentIsinsAndGenerateCandlesticks() {
        // Data prep
        LocalDateTime now = LocalDateTime.now().withSecond(0);
        List<Quote> quotes = Arrays.asList(
                new Quote(1, 1.0, "isbnk", now.plusSeconds(5)),
                new Quote(2, 1.1, "isbnk", now.plusSeconds(10)),
                new Quote(3, 3.1, "isbnk", now.plusSeconds(15)),
                new Quote(4, 5.9, "isbnk", now.plusSeconds(30)),
                new Quote(1, 1.0, "garan", now.plusSeconds(5)),
                new Quote(2, 1.1, "garan", now.plusSeconds(10)),
                new Quote(3, 3.1, "garan", now.plusSeconds(15))
        );
        var candlesticks = Arrays.asList(
                Candlestick.builder()
                        .id(1)
                        .openingTimestamp(now)
                        .isin("isbnk")
                        .openingPrice(1.0)
                        .highestPrice(5.9)
                        .lowestPrice(1.0)
                        .closingPrice(5.9)
                        .closingTimestamp(now.plusSeconds(30))
                        .build(),
                Candlestick.builder()
                        .id(2)
                        .openingTimestamp(now)
                        .isin("garan")
                        .openingPrice(1.0)
                        .highestPrice(3.1)
                        .lowestPrice(1.0)
                        .closingPrice(3.1)
                        .closingTimestamp(now.plusSeconds(15))
                        .build()
        );
        // Stubbing
        when(quoteRepository.findAllByDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(quotes);
        when(candlestickRepository.saveAll(anyList())).thenReturn(candlesticks);
        // Interaction
        var result = task.persistCandlestick();
        // Assertion
        assertThat(result).isNotEmpty()
                .hasSize(2)
                .isEqualTo(candlesticks)
                .element(0)
                .isInstanceOf(Candlestick.class);
    }
}