package com.zlk.item;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = {"com.zlk.item.mapper"})
@SpringBootApplication
class CommonItemTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonItemTestApplication.class, args);
    }

}
