package org.mehmetcc.candlesticksweb.candlestick;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CandlestickService {
    public record CandlestickServiceException(String message, HttpStatus status) {
    }

    private final CandlestickRepository repository;

    public Either<CandlestickServiceException, List<Candlestick>> retrieveCandlesticks(String isin) {
        var candlesticks = retrieveFromDatabase(isin);
        if (candlesticks.isEmpty())
            return Either.left(new CandlestickServiceException("No entries found!", HttpStatus.BAD_REQUEST));
        var updated = fillRemaining(candlesticks);
        return Either.right(updated);
    }

    private List<Candlestick> fillRemaining(List<Candlestick> candlesticks) {
        var latest = candlesticks.get(candlesticks.size() - 1);

        while (candlesticks.size() < 30) {
            candlesticks.add(latest);
        }
        return candlesticks;
    }

    private List<Candlestick> retrieveFromDatabase(String isin) {
        return new ArrayList<>(repository
                .findAllByIsin(isin)
                .stream()
                .sorted(Comparator.comparing(Candlestick::getOpeningTimestamp))
                .limit(30) // TODO configure
                .toList());
    }
}
