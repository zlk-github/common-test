package com.zlk.db;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = {"com.zlk.*"})
@SpringBootApplication
class CommonDbTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonDbTestApplication.class, args);
    }

}
