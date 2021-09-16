package com.zlk.db;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@MapperScan(basePackages = {"com.zlk.db.mapper"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
class CommonDbTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonDbTestApplication.class, args);
    }

}
