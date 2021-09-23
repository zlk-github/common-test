package com.zlk.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

//扫描到配置的redis工具类与配置类及其他需要spring管理的bean
@ComponentScan(basePackages={"com.zlk"})
//需要排除阿里默认数据源，否则启动报错
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

}
