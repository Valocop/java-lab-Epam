package com.epam.lab.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Configuration
@ComponentScan({"com.epam.lab"})
@PropertySource("classpath:app.properties")
public class SpringThreadUtilityConfiguration {

    @Bean
    public ValidatorFactory validatorFactory() {
        return Validation.buildDefaultValidatorFactory();
    }

    @Bean
    public Validator validator(ValidatorFactory validatorFactory) {
        return validatorFactory.getValidator();
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

    @Bean("writingQueue")
    public BlockingQueue<String> getWritingQueue(@Value("${FILES_COUNT}") int capacity) {
        return new ArrayBlockingQueue<>(capacity);
    }

    @Bean("readingQueue")
    public BlockingQueue<Path> getReadingQueue(@Value("${FILES_COUNT}") int capacity) {
        return new ArrayBlockingQueue<>(capacity);
    }
}
