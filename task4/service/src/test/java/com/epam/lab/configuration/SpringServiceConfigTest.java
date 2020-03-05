package com.epam.lab.configuration;

import com.epam.lab.repository.NewsRepository;
import com.epam.lab.service.AuthorService;
import com.epam.lab.service.NewsServiceImplTest;
import com.epam.lab.service.TagService;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = NewsServiceImplTest.class)
public class SpringServiceConfigTest {

    @Bean
    public NewsRepository newsRepository() {
        return Mockito.mock(NewsRepository.class);
    }

    @Bean
    public AuthorService authorService() {
        return Mockito.mock(AuthorService.class);
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
