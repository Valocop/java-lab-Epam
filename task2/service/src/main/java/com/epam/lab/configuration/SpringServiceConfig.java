package com.epam.lab.configuration;

import com.epam.lab.repository.AuthorRepo;
import com.epam.lab.repository.NewsRepo;
import com.epam.lab.repository.TagRepo;
import com.epam.lab.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SpringRepoConfig.class)
public class SpringServiceConfig {

    @Bean
    public NewsService newsService(NewsRepo newsRepo) {
        NewsServiceImpl newsService = new NewsServiceImpl(newsRepo);
        return newsService;
    }

    @Bean
    public AuthorService authorService(AuthorRepo authorRepo) {
        return new AuthorServiceImpl(authorRepo);
    }

    @Bean
    public TagService tagService(TagRepo tagRepo) {
        TagServiceImpl tagService = new TagServiceImpl(tagRepo);
        tagService.setModelMapper(modelMapper());
        return tagService;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
