package com.epam.lab.configuration;

import com.epam.lab.repository.AuthorRepository;
import com.epam.lab.repository.NewsRepository;
import com.epam.lab.repository.TagRepository;
import com.epam.lab.service.*;
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
    public AuthorService authorService(AuthorRepository authorRepository) {
        return new AuthorServiceImpl(authorRepository);
    }

    @Bean
    public NewsService newsService(NewsRepository newsRepository, AuthorRepository authorRepository,
                                   TagRepository tagRepository) {
        return new NewsServiceImpl(newsRepository, authorRepository, tagRepository);
    }

    @Bean
    public TagService tagService(TagRepository tagRepository) {
        return new TagServiceImpl(tagRepository);
    }
}
