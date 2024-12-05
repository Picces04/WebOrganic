package com.projectweb.controller.home.Format;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThymeleafConfig {
    @Bean
    public NumberFormatter numberFormatter() {
        return new NumberFormatter();
    }
}
