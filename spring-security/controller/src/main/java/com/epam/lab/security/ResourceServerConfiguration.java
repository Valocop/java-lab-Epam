package com.epam.lab.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf().disable()
                .antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/api/users").anonymous()
                .antMatchers(HttpMethod.GET, "/api/news/**", "/api/authors/**", "/api/tags/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.PUT, "/api/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated();
    }
}
