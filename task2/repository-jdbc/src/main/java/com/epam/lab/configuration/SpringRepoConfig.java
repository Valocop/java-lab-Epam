package com.epam.lab.configuration;

import com.epam.lab.repository.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@ComponentScan("com.epam.lab")
public class SpringRepoConfig {

    @Bean
    public DataSource dataSource() throws IOException {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("database.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            HikariConfig hikariConfig = new HikariConfig(properties);
            return new HikariDataSource(hikariConfig);
        }
    }

    @Bean
    public AuthorRepo authorRepo(DataSource dataSource) {
        return new AuthorRepoImpl(dataSource);
    }

    @Bean
    public NewsRepo newsRepo(DataSource dataSource) {
        return new NewsRepoImpl(dataSource);
    }

    @Bean
    public TagRepo tagRepo(DataSource dataSource) {
        return new TagRepoImpl(dataSource);
    }
}
