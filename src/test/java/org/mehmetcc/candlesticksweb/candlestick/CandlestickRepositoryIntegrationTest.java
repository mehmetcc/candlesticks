package org.mehmetcc.candlesticksweb.candlestick;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class CandlestickRepositoryIntegrationTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CandlestickRepository repository;

    @Test
    void shouldPersistSingleEntity() {
        // Data prep
        LocalDateTime now = LocalDateTime.now().withSecond(0);
        var candlesticks = Arrays.asList(
                Candlestick.builder()
                        .id(1)
                        .openingTimestamp(now)
                        .isin("isbnk")
                        .openingPrice(1.0)
                        .highestPrice(5.9)
                        .lowestPrice(1.0)
                        .closingPrice(5.9)
                        .closingTimestamp(now.plusSeconds(30))
                        .build(),
                Candlestick.builder()
                        .id(2)
                        .openingTimestamp(now)
                        .isin("garan")
                        .openingPrice(1.0)
                        .highestPrice(3.1)
                        .lowestPrice(1.0)
                        .closingPrice(3.1)
                        .closingTimestamp(now.plusSeconds(15))
                        .build()
        );
        // Interaction
        var created = repository.saveAll(candlesticks);
        // Assertion
        assertThat(created).isNotNull()
                .hasSize(2)
                .element(0)
                .isInstanceOf(Candlestick.class);
    }

    @Test
    void shouldFilterByIsin() {
        // Data prep
        LocalDateTime now = LocalDateTime.now().withSecond(0);
        var isbnk = Candlestick.builder()
                .id(1)
                .openingTimestamp(now)
                .isin("isbnk")
                .openingPrice(1.0)
                .highestPrice(5.9)
                .lowestPrice(1.0)
                .closingPrice(5.9)
                .closingTimestamp(now.plusSeconds(30))
                .build();
        var garan = Candlestick.builder()
                .id(2)
                .openingTimestamp(now)
                .isin("garan")
                .openingPrice(1.0)
                .highestPrice(3.1)
                .lowestPrice(1.0)
                .closingPrice(3.1)
                .closingTimestamp(now.plusSeconds(15))
                .build();
        var candlesticks = Arrays.asList(isbnk, garan);
        // Interaction
        var found = repository.findAllByIsin("garan");
        // Assertion
        assertThat(found).isNotNull().isEqualTo(garan);
    }
}