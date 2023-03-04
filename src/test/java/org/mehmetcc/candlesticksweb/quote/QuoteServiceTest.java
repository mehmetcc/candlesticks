package org.mehmetcc.candlesticksweb.quote;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class QuoteServiceTest {
    @Mock
    private QuoteRepository repository;

    @InjectMocks
    private QuoteService service;


    @Test
    void validJsonGiven_shouldPersistData() {
        // Data prep
        var json = "{\"data\": {\"price\": 1145.1385,\"isin\": \"VNB844277627\"},\"type\": \"QUOTE\"}";
        var expected = new Quote(1, 1.1, "isbnk", LocalDateTime.now());
        // Stubbing
        when(repository.save(any(Quote.class))).thenReturn(expected);
        // Interaction
        var event = service.persist(json);
        // Assertion
        assertThat(event).isNotNull()
                .isEqualTo(expected);
    }

    @Test
    void invalidJsonGiven_shouldThrowException() {
        // Data prep
        var json = "{\"data\": {\"sffsddsffds\": 1145.1385,\"isin\": \"VNB844277627\"},\"type\": \"QUOTE\"}";
        // Interaction
        var caught = assertThrows(NullPointerException.class, () -> service.persist(json));
        // Assertions
        assertThat(caught).isInstanceOf(NullPointerException.class);
    }
}