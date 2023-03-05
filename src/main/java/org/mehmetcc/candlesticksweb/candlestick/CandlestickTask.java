package org.mehmetcc.candlesticksweb.candlestick;

import org.mehmetcc.candlesticksweb.quote.Quote;
import org.mehmetcc.candlesticksweb.quote.QuoteRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
public class CandlestickTask {
    private final CandlestickRepository candlestickRepository;

    private final QuoteRepository quoteRepository;

    public CandlestickTask(final CandlestickRepository candlestickRepository, final QuoteRepository quoteRepository) {
        this.candlestickRepository = candlestickRepository;
        this.quoteRepository = quoteRepository;
    }

    @Scheduled(cron = "0 * * * * *")
    public List<Candlestick> persistCandlestick() {
        var now = LocalDateTime.now().withSecond(0);
        var end = now.plusMinutes(1);
        var fetched = quoteRepository.findAllByDateBetween(now, end);

        var grouped = fetched.stream()
                .collect(Collectors.groupingBy(Quote::getIsin)); // lmao streams don't do groupBy
        var candlesticks = grouped.entrySet().parallelStream().map(current ->
                        Candlestick.builder()
                                .openingTimestamp(now)
                                .isin(current.getKey())
                                .openingPrice(openingPrice(current.getValue()))
                                .highestPrice(highestPrice(current.getValue()))
                                .lowestPrice(lowestPrice(current.getValue()))
                                .closingPrice(closingPrice(current.getValue()))
                                .closingTimestamp(end)
                                .build())
                .toList();
        return candlestickRepository.saveAll(candlesticks);
    }

    private Double openingPrice(List<Quote> quotes) {
        quotes.sort(Comparator.comparing(Quote::getDate).reversed());
        return quotes.stream()
                .findFirst()
                .orElseThrow(NoSuchElementException::new)
                .getPrice();
    }

    private Double closingPrice(List<Quote> quotes) {
        quotes.sort(Comparator.comparing(Quote::getDate));
        return quotes.stream()
                .findFirst()
                .orElseThrow(NoSuchElementException::new)
                .getPrice();
    }

    private Double highestPrice(List<Quote> quotes) {
        return quotes.stream()
                .mapToDouble(Quote::getPrice)
                .max()
                .orElseThrow(NoSuchElementException::new);
    }

    private Double lowestPrice(List<Quote> quotes) {
        return quotes.stream()
                .mapToDouble(Quote::getPrice)
                .min()
                .orElseThrow(NoSuchElementException::new);
    }
}
