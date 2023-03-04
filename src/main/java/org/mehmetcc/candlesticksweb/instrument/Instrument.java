package org.mehmetcc.candlesticksweb.instrument;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Instrument {
    @Id
    private String isin;

    private String description;
}
