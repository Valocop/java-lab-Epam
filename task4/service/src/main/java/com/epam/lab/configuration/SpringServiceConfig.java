package com.epam.lab.configuration;

import com.epam.lab.repository.AuthorRepository;
import com.epam.lab.repository.NewsRepository;
import com.epam.lab.repository.TagRepository;
import com.epam.lab.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
@ComponentScan("com.epam.lab")
@Import(SpringRepoConfig.class)
public class SpringServiceConfig {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean
    public AuthorService authorService(AuthorRepository authorRepository, ModelMapper modelMapper) {
        return new AuthorServiceImpl(authorRepository, modelMapper);
    }

    @Bean
    public NewsService newsService(NewsRepository newsRepository, AuthorService authorService,
                                   TagService tagService, ModelMapper modelMapper) {
        return new NewsServiceImpl(newsRepository, authorService, tagService, modelMapper);
    }

    @Bean
    public TagService tagService(TagRepository tagRepository, ModelMapper modelMapper) {
        return new TagServiceImpl(tagRepository, modelMapper);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
