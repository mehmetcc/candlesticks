package org.mehmetcc.candlesticksweb.instrument;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
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

    public Either<String, InstrumentEvent> match(String payload) {
        return readJson(payload)
                .flatMap(this::checkNullity)
                .flatMap(this::matchEventType);
    }

    private Either<String, InstrumentEvent> readJson(String payload) {
        InstrumentEvent json;
        try {
            json = mapper.readValue(payload, InstrumentEvent.class);
        } catch (JsonProcessingException error) {
            return Either.left(error.getMessage());
        }
        return Either.right(json);
    }

    Either<String, InstrumentEvent> checkNullity(InstrumentEvent event) {
        if (Objects.isNull(event)) return Either.left(new NullPointerException().getMessage());
        return Either.right(event);
    }

    private Either<String, InstrumentEvent> matchEventType(InstrumentEvent event) {
        if (event.getType() == InstrumentEventType.DELETE) {
            repository.deleteById(event.getData().getIsin());
        }
        repository.save(event.getData());
        return Either.right(event);
    }
}
