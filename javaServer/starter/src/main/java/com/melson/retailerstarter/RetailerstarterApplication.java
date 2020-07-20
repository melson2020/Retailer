package com.melson.retailerstarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Configuration
@EnableScheduling
@ComponentScan(basePackages = { "com.melson.webserver","com.melson.wechatmini","com.melson.retailerstarter"})
public class RetailerstarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetailerstarterApplication.class, args);
    }

}
