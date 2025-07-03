package ru.fishexam.fishexam.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;

@Configuration
public class DbConfiguration {

    @Bean
    public PostgreSqlJdbcTemplate postgreSqlJdbcTemplate(DataSource dataSource) {
        return new PostgreSqlJdbcTemplate(dataSource);
    }

}
