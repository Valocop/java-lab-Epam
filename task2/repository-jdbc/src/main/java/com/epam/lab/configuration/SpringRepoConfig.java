package com.epam.lab.configuration;

import com.epam.lab.repository.AuthorRepoImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public AuthorRepoImpl authorRepo(DataSource jdbcTemplate) {
        return new AuthorRepoImpl(jdbcTemplate);
    }
}
