package org.mehmetcc.candlesticksweb.instrument;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class InstrumentRepositoryIntegrationTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private InstrumentRepository repository;

    @Test
    void shouldPersistSingleEntity() {
        var expected = new Instrument("isin", "isinisin");
        var found = repository.save(expected);

        assertThat(found).isNotNull().isEqualTo(expected);
    }

    @Test
    void shouldDeleteSingleEntity() {
        var instrument = new Instrument("isin", "isinisin");
        var persisted = repository.save(instrument);
        repository.delete(instrument);
        var optional = repository.findById(instrument.getIsin());

        assertThat(optional).isEmpty();
    }
}