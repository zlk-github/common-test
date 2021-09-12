package com.zlk.db.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
// 注 类和方法必须为public才能运行
@RunWith(SpringRunner.class)
public class CommonDbTestApplicationTests {


    @Test
    public void testFunction() {
        System.out.println("测试用例");
    }

}
