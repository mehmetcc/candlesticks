package org.mehmetcc.candlesticksweb.instrument;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class InstrumentServiceTest {
    @Mock
    private InstrumentRepository repository;

    @InjectMocks
    private InstrumentService service;

    @Test
    void validJsonWithAddTypeGiven_shouldPersistData() {
        // Data prep
        var json = "{\"type\":\"ADD\",\"data\":{\"isin\":\"LS342I184454\",\"description\":\"elementum eos accumsan orci constituto antiopam\"}}";
        var persisted = new Instrument("LS342I184454", "elementum eos accumsan orci constituto antiopam");
        // Interaction
        var event = service.match(json);
        var got = event.get();
        // Assertion
        assertThat(got.getType())
                .isNotNull()
                .isEqualTo(InstrumentEventType.ADD);
        assertThat(got.getData())
                .isNotNull()
                .isInstanceOf(Instrument.class)
                .isEqualTo(persisted);
    }

    @Test
    void validJsonWithDeleteTypeGiven_shouldDeleteData() {
        // Data prep
        var json = "{\"type\":\"DELETE\",\"data\":{\"isin\":\"LS342I184454\",\"description\":\"elementum eos accumsan orci constituto antiopam\"}}";
        var deleted = new Instrument("LS342I184454", "elementum eos accumsan orci constituto antiopam");
        // Interaction
        var event = service.match(json);
        var got = event.get();
        // Assertion
        assertThat(got.getType())
                .isNotNull()
                .isEqualTo(InstrumentEventType.DELETE);
        assertThat(got.getData())
                .isNotNull()
                .isInstanceOf(Instrument.class)
                .isEqualTo(deleted);
    }

    @Test
    void invalidJsonGiven_shouldCapsulateLeftProjection() {
        // Data prep
        var json = "{i am illegal}";
        // Interaction
        var caught = service.match(json);
        // Assertions
        assertThat(caught.isLeft()).isTrue();
    }
}
