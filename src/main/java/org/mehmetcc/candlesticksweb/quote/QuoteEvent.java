package org.mehmetcc.candlesticksweb.quote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteEvent {
    private QuoteType type;

    private Quote data;
}
