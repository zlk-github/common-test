package com.zlk.redis.util;


import com.zlk.common.redis.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author likuan.zhou
 * @title: IUserServiceTest
 * @projectName common-test
 * @description: 用户业务测试
 * @date 2021/9/15/015 19:37
 */
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
// 注 类和方法必须为public才能运行
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisUtilTest {
    @Autowired
    private RedisUtil redisUtil;
    private final static String STR_KEY = "str:key";

    @Test
    public void testPageList1() {
      /*  redisUtil.set(STR_KEY);
        assertNotNull(pageList);*/
    }

  /*  private static List<UserVo> getUserList(){

    }*/


}
