package org.mehmetcc.candlesticksweb.quote;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Quote {
    @Id
    @GeneratedValue
    private Integer id;

    private Double price;

    private String isin;

    @Column(name = "received_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date;
}
