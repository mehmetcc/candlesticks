package org.mehmetcc.candlesticksweb.quote;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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

    private LocalDateTime date;
}