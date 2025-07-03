package ru.fishexam.fishexam.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;

@Configuration
public class DbConfiguration {

    @Bean
    public PostgreSqlJdbcTemplate mainDb(DataSource dataSource) {
        return new PostgreSqlJdbcTemplate(dataSource);
    }

    @Bean
    public JdbcTransactionManager mainDbTransactionManager(DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }
}
