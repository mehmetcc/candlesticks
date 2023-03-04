package org.mehmetcc.candlesticksweb.instrument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentEvent {
    private InstrumentEventType type;

    private Instrument data;
}
