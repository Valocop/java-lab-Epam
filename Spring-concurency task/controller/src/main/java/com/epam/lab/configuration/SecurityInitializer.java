package com.epam.lab.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@Configuration
@Order(2)
public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {
}
