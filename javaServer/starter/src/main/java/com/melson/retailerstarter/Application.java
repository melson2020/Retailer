package com.melson.retailerstarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by Nelson on 2020/6/5.
 */
@Configuration
@EnableScheduling
@EnableJpaRepositories(basePackages = {"com.melson.base.dao","com.melson.webserver.dao","com.melson.wechatmini.dao"})//jpa repositry 路径
@EntityScan(basePackages = {"com.melson.base.entity","com.melson.webserver.entity","com.melson.wechatmini.entity"}) // 3. Entity 所在的包
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * 工程打成war需要继承SpringBootServletInitializer，重写configure方法
     * @date 2018年12月7日
     * @author zxp
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(RetailerstarterApplication.class);
    }
}
