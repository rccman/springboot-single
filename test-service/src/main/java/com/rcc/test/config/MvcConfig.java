package com.rcc.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.convert.converter.Converter;

import java.util.HashSet;
import java.util.Set;

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

    /**
     * 自定义 日期数据转换器
     * @return
     */
    @Bean
    public ConversionServiceFactoryBean conversionService(){
        ConversionServiceFactoryBean conversionServiceFactoryBean=new ConversionServiceFactoryBean();
        Set<Converter> converters=new HashSet<Converter>();
        //日期转换器
        converters.add(new DateConverter());
        conversionServiceFactoryBean.setConverters(converters);
        return conversionServiceFactoryBean;
    }

}
