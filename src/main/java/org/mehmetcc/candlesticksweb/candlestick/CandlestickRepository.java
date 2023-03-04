package org.mehmetcc.candlesticksweb.candlestick;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandlestickRepository extends JpaRepository<Candlestick, String> {
    List<Candlestick> findAllByIsin(String isin);
}
