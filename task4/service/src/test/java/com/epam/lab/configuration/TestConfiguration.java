package com.epam.lab.configuration;

import com.epam.lab.repository.TagRepository;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class TestConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ModelMapper mapperUtil(ModelMapper modelMapper) {
        return new ModelMapper();
    }

    @Bean
    public TagRepository tagRepository(TagRepository tagRepository) {
        return Mockito.spy(tagRepository);
    }
}
