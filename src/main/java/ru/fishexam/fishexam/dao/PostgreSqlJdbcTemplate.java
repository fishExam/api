package ru.fishexam.fishexam.dao;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class PostgreSqlJdbcTemplate extends JdbcTemplate {

    public PostgreSqlJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }

    public <T> Optional<T> queryForObjectOptional(String sql, RowMapper<T> rowMapper, Object... args) {
        List<T> results = query(sql, rowMapper, args);
        return results.isEmpty()
                ? Optional.empty()
                : Optional.of(results.getFirst());
    }
}
