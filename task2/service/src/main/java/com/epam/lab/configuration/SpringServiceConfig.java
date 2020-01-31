package com.epam.lab.configuration;

import com.epam.lab.repository.NewsRepo;
import com.epam.lab.service.NewsService;
import com.epam.lab.service.NewsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SpringRepoConfig.class)
public class SpringServiceConfig {

    @Bean
    public NewsService newsService(NewsRepo newsRepo) {
        return new NewsServiceImpl(newsRepo);
    }
}
