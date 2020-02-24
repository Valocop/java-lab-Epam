package com.epam.lab.configuration;

import com.epam.lab.controller.AuthorController;
import com.epam.lab.controller.NewsController;
import com.epam.lab.controller.TagController;
import com.epam.lab.exception.NewsExceptionHandler;
import com.epam.lab.service.AuthorService;
import com.epam.lab.service.NewsService;
import com.epam.lab.service.TagService;
import com.epam.lab.validation.SearchValidator;
import com.epam.lab.validation.SortValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.text.SimpleDateFormat;

@Configuration
@EnableWebMvc
@ComponentScan("com.epam.lab")
@Import(SpringServiceConfig.class)
public class SpringControllerConfig {

    @Bean
    public NewsController newsController(NewsService newsService) {
        NewsController newsController = new NewsController(newsService);
        newsController.setSearchValidator(searchValidator());
        newsController.setSortValidator(sortValidator());
        return newsController;
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
    public NewsExceptionHandler newsExceptionHandler() {
        return new NewsExceptionHandler();
    }

    @Bean
    public SearchValidator searchValidator() {
        return new SearchValidator();
    }

    @Bean
    public SortValidator sortValidator() {
        return new SortValidator();
    }

    @Bean
    public MappingJackson2HttpMessageConverter getJsonMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
        converter.setPrettyPrint(true);
        return converter;
    }

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return builder;
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }
}
