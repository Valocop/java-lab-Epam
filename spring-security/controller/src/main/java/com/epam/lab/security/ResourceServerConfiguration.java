package com.epam.lab.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/users**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/users").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/users/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/news*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/news/**").permitAll()
                .antMatchers(HttpMethod.GET).hasRole("USER")
                .antMatchers(HttpMethod.PUT).hasRole("USER")
                .antMatchers(HttpMethod.DELETE).hasRole("ADMIN")
                .antMatchers(HttpMethod.POST).hasRole("USER")
                .anyRequest().authenticated();
    }
}
