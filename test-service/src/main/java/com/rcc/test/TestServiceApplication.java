package com.rcc.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*@EnableEurekaClient
@EnableFeignClients
@EnableDiscoveryClient*/
@SpringBootApplication
public class TestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestServiceApplication.class, args);
    }

}
