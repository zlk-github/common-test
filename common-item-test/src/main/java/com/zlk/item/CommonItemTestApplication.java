package com.zlk.item;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@MapperScan(basePackages = {"com.zlk.item.mapper"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
class CommonItemTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonItemTestApplication.class, args);
    }

}
