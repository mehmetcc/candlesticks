package org.mehmetcc.candlesticksweb.instrument;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class InstrumentService {
    private final InstrumentRepository repository;

    private final ObjectMapper mapper;

    public InstrumentService(final InstrumentRepository repository) {
        this.repository = repository;
        this.mapper = new ObjectMapper();
    }

    public InstrumentEvent match(String payload) {
        var event = readJson(payload);
        return matchEventType(event);
    }

    private InstrumentEvent readJson(String payload) {
        InstrumentEvent json = null;
        try {
            json = mapper.readValue(payload, InstrumentEvent.class);
        } catch (JsonProcessingException error) {
            log.error(error.getMessage());
        }
        return json;
    }

    private InstrumentEvent matchEventType(InstrumentEvent event) {
        Objects.requireNonNull(event);
        if (event.getType() == InstrumentEventType.DELETE) {
            repository.deleteById(event.getData().getIsin());
        }
        repository.save(event.getData());
        return event;
    }
}
