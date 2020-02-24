package com.epam.lab.configuration;

import com.epam.lab.repository.AuthorRepo;
import com.epam.lab.repository.NewsRepo;
import com.epam.lab.service.AuthorService;
import com.epam.lab.service.AuthorServiceImpl;
import com.epam.lab.service.NewsService;
import com.epam.lab.service.TagService;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringServiceTestConfig {

    @Bean
    public AuthorRepo authorRepo() {
        return Mockito.mock(AuthorRepo.class);
    }

    @Bean
    public NewsRepo newsRepo() {
        return Mockito.mock(NewsRepo.class);
    }

    @Bean
    public NewsService newsService() {
        return Mockito.mock(NewsService.class);
    }

    @Bean
    public AuthorService authorService(AuthorRepo authorRepo, NewsRepo newsRepo) {
        AuthorServiceImpl authorService = new AuthorServiceImpl(authorRepo, newsRepo);
        authorService.setModelMapper(modelMapper());
        return authorService;
    }

    @Bean
    public TagService tagService() {
        return Mockito.mock(TagService.class);
    }

    @Bean
    public ModelMapper modelMapper() {
        return Mockito.mock(ModelMapper.class);
    }
}
