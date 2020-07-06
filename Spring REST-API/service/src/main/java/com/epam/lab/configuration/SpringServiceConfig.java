package com.epam.lab.configuration;

import com.epam.lab.repository.AuthorRepo;
import com.epam.lab.repository.NewsRepo;
import com.epam.lab.repository.TagRepo;
import com.epam.lab.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.epam.lab")
@Import(SpringRepoConfig.class)
public class SpringServiceConfig {

    @Bean
    public NewsService newsService(NewsRepo newsRepo, AuthorService authorService, TagService tagService) {
        NewsServiceImpl newsService = new NewsServiceImpl(newsRepo, authorService, tagService);
        newsService.setModelMapper(modelMapper());
        return newsService;
    }

    @Bean
    public AuthorService authorService(AuthorRepo authorRepo, NewsRepo newsRepo) {
        AuthorServiceImpl authorService = new AuthorServiceImpl(authorRepo, newsRepo);
        authorService.setModelMapper(modelMapper());
        return authorService;
    }

    @Bean
    public TagService tagService(TagRepo tagRepo, NewsRepo newsRepo) {
        TagServiceImpl tagService = new TagServiceImpl(tagRepo, newsRepo);
        tagService.setModelMapper(modelMapper());
        return tagService;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
