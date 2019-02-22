package com.rcc.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MvcConfig {
    /**
     * 国际化message配置
     * @return
     */
    @Bean(name = "errorMessageSource")
    public ResourceBundleMessageSource resourceBundleMessageSource() {
        ResourceBundleMessageSource errorMessageSource = new ResourceBundleMessageSource();
        errorMessageSource.addBasenames("messages/messages");
        return errorMessageSource;
    }
}
