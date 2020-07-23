package com.melson.retailerstarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.melson"})
@Configuration
@EnableScheduling
@EnableJpaRepositories(basePackages = {"com.melson.base.dao"})//jpa repositry 路径
@EntityScan(basePackages = {"com.melson.base.entity"}) // 3. Entity 所在的包
public class RetailerstarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetailerstarterApplication.class, args);
    }

}
