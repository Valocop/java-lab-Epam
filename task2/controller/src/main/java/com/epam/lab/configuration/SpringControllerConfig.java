package com.epam.lab.configuration;

import com.epam.lab.controller.AuthorController;
import com.epam.lab.controller.NewsController;
import com.epam.lab.controller.TagController;
import com.epam.lab.exception.ExceptionHandler;
import com.epam.lab.service.AuthorService;
import com.epam.lab.service.NewsService;
import com.epam.lab.service.TagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@Import(SpringServiceConfig.class)
public class SpringControllerConfig {
    private static final Logger log = LogManager.getLogger();

    @Bean
    public NewsController newsController(NewsService newsService) {
        return new NewsController(newsService);
    }

    @Bean
    public AuthorController authorController(AuthorService authorService) {
        return new AuthorController(authorService);
    }

    @Bean
    public TagController tagController(TagService tagService) {
        return new TagController(tagService);
    }

    @Bean
    public ExceptionHandler exceptionHandler() {
        return new ExceptionHandler();
    }

//    @Bean
//    public MethodValidationPostProcessor methodValidationPostProcessor() {
//        return new MethodValidationPostProcessor();
//    }
}
