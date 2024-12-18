package org.yearup.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

@Configuration
public class LoggingConfig {
    @Bean
    public Logger logger() {
        return Logger.getLogger("ApplicationLogger");
    }
} 