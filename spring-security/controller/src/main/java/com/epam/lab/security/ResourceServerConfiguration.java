package com.epam.lab.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "resource_id";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
//        http
//                .anonymous().disable()
//                .authorizeRequests()
//                .antMatchers("api/users/**").authenticated()
//                .antMatchers("api/authors/**").authenticated()
//                .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
        http
                .cors(Customizer.withDefaults())
                .csrf().disable()
                .antMatcher("/api/**")
//                .antMatcher("/**")
                .authorizeRequests()
//                .antMatchers("/oauth/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/oauth/**").permitAll()
//                .antMatchers(HttpMethod.OPTIONS, "/oauth/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/users**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/api/users").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/users/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/news*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/news/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/authors/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/tags/**").permitAll()
                .antMatchers(HttpMethod.GET).hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.PUT).hasRole("USER")
                .antMatchers(HttpMethod.DELETE).hasRole("ADMIN")
                .antMatchers(HttpMethod.POST).hasRole("USER")
                .anyRequest().authenticated();
    }
}
