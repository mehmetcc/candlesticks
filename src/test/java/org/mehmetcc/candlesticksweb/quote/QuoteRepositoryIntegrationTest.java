package org.mehmetcc.candlesticksweb.quote;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class QuoteRepositoryIntegrationTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private QuoteRepository repository;

    @Test
    void shouldPersistSingleEntity() {
        var expected = new Quote(1, 31.31, "isinisin", LocalDateTime.now());
        var found = repository.save(expected);

        assertThat(found).isNotNull().isEqualTo(expected);
    }

    @Test
    void shouldFindQuotesInBetween() {
        // Data prep
        var start = LocalDateTime.of(
                LocalDate.of(2022, 8, 4),
                LocalTime.of(20, 0, 0));
        var end = start.plusSeconds(10);
        var first = new Quote(1, 31.31, "isinisin", start);
        var second = new Quote(10, 31.321, "newisinsds", end);
        repository.saveAll(Arrays.asList(first, second));
        // Interaction
        var result = repository.findAllByDateBetween(start, start.plusMinutes(1));
        // Assertion
        assertThat(result).isInstanceOf(List.class)
                .hasSize(2);
    }
}