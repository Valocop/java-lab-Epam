package com.epam.lab.configuration;

import com.epam.lab.controller.NewsController;
import com.epam.lab.service.NewsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@Import(SpringServiceConfig.class)
public class SpringControllerConfig {

    @Bean
    public NewsController newsController(NewsService newsService) {
        return new NewsController(newsService);
    }
}
