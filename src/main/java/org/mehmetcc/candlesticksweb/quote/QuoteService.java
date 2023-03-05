package org.mehmetcc.candlesticksweb.quote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
public class QuoteService {
    private final QuoteRepository repository;

    private final ObjectMapper mapper;

    public QuoteService(final QuoteRepository repository) {
        this.repository = repository;
        this.mapper = new ObjectMapper();
    }

    public Either<String, Quote> match(String payload) {
        return readJson(payload)
                .flatMap(this::checkNullity)
                .map(this::persist);
    }

    private Quote persist(QuoteEvent event) {
        var data = event.getData();
        data.setDate(LocalDateTime.now());
        return repository.save(data);
    }

    private Either<String, QuoteEvent> readJson(String payload) {
        QuoteEvent json;
        try {
            json = mapper.readValue(payload, QuoteEvent.class);
        } catch (JsonProcessingException error) {
            return Either.left(error.getMessage());
        }
        return Either.right(json);
    }

    private Either<String, QuoteEvent> checkNullity(QuoteEvent event) {
        if (Objects.isNull(event)) return Either.left(new NullPointerException().getMessage());
        return Either.right(event);
    }
}
