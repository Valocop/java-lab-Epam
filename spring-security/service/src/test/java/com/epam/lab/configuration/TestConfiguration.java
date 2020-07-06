package com.epam.lab.configuration;

import com.epam.lab.repository.AuthorRepository;
import com.epam.lab.repository.NewsRepository;
import com.epam.lab.repository.TagRepository;
import com.epam.lab.service.NewsService;
import com.epam.lab.service.NewsServiceImpl;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class TestConfiguration {

    @Bean("mockNewsRepository")
    public NewsRepository newsRepository(@Qualifier("newsRepository") NewsRepository newsRepository) {
        return Mockito.mock(NewsRepository.class, AdditionalAnswers.delegatesTo(newsRepository));
    }

    @Bean("testNewsService")
    public NewsService newsService(@Qualifier("mockNewsRepository") NewsRepository newsRepository,
                                   AuthorRepository authorRepository,
                                   TagRepository tagRepository) {
        return new NewsServiceImpl(newsRepository, authorRepository, tagRepository);
    }
}
