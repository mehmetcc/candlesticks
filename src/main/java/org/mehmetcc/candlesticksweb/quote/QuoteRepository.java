package org.mehmetcc.candlesticksweb.quote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Integer> {
    List<Quote> findAllByDateBetween(LocalDateTime publicationTimeStart, LocalDateTime publicationTimeEnd);
}
