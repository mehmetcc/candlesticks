package org.mehmetcc.candlesticksweb.candlestick;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Candlestick {
    @Id
    @GeneratedValue
    private Integer id;

    private LocalDateTime openingTimestamp;

    private String isin;

    private Double openingPrice;

    private Double highestPrice;

    private Double lowestPrice;

    private Double closingPrice;

    private LocalDateTime closingTimestamp;
}
