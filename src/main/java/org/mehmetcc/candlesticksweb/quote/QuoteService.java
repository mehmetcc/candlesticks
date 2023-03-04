package org.mehmetcc.candlesticksweb.quote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public Quote persist(String payload) {
        var event = readJson(payload);
        var data = event.getData();
        data.setDate(LocalDateTime.now());
        return repository.save(data);
    }

    private QuoteEvent readJson(String payload) {
        QuoteEvent json = null;
        try {
            json = mapper.readValue(payload, QuoteEvent.class);
        } catch (JsonProcessingException error) {
            log.error("Client transport error: {}", error.getMessage());
        }
        Objects.requireNonNull(json);
        return json;
    }
}
