package com.epam.lab.configuration;

import com.epam.lab.dao.AuthorDaoImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class SpringRepoConfig {

    @Bean
    public DataSource dataSource() throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            HikariConfig hikariConfig = new HikariConfig(properties);
            return new HikariDataSource(hikariConfig);
        }
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public AuthorDaoImpl authorDao(JdbcTemplate jdbcTemplate) {
        return new AuthorDaoImpl(jdbcTemplate);
    }
}
