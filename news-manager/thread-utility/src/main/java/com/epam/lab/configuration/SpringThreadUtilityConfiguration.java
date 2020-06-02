package com.epam.lab.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.text.SimpleDateFormat;

@Configuration
@ComponentScan({"com.epam.lab.thread"})
@PropertySource("classpath:app.properties")
public class SpringThreadUtilityConfiguration {

//    @Bean
//    public MappingJackson2HttpMessageConverter getJsonMessageConverter(ObjectMapper objectMapper) {
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
//        converter.setPrettyPrint(true);
//        return converter;
//    }
//
//    @Bean
//    public Jackson2ObjectMapperBuilder jacksonBuilder() {
//        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
//        builder.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
//        return builder;
//    }

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
}
