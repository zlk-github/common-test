package com.zlk.redis.redisson;

import com.zlk.common.redis.redisson.IRedissonLock;
import com.zlk.redis.constant.RedissonConstant;
import io.swagger.annotations.ApiOperation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.RedissonLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author likuan.zhou
 * @title: RedissonLockImplTest
 * @projectName common-test
 * @description: Redisson 测试类
 * @date 2021/10/9/009 9:12
 */
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
// 注 类和方法必须为public才能运行
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedissonLockImplTest {

    @Autowired
    private IRedissonLock redissonLock;

    //===============================可重入锁（Reentrant Lock）=====================================
    //基于Redis的Redisson分布式可重入锁RLock Java对象实现了java.util.concurrent.locks.Lock接口。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口
    // 可重入锁: 避免死锁，可重复递归调用的锁,同一线程外层函数获取锁后,内层递归函数仍然可以获取锁,并且不发生死锁(前提是同一个对象或者class)

    // 加锁方法不要使用测试用例测试，要不然方法执行结束相当于宕机，无法为锁续命（到期就会自动失效）。

    @Test
    public void testAddLock() {
        //加可重入锁-默认30S
        //Redisson实例被关闭前，不断的延长锁的有效期。默认情况下，看门狗的检查锁的超时时间是30秒钟，也可以通过修改Config.lockWatchdogTimeout来另行指定。
        Boolean bl = redissonLock.addLock(RedissonConstant.LOCK_KEY);
        assertEquals(true,bl);
    }

    @Test
    public void testRemoveLock() {
        //手动释放可重入锁
        Boolean bl = redissonLock.removeLock(RedissonConstant.LOCK_KEY);
        assertEquals(true,bl);
    }

    //可重入锁--等待失效类型 (等待效果不容易生效)
    @Test
    public void testAddTryLock() throws InterruptedException {
        for (int i = 0; i < 500; i++) {
            int finalI = i;
            Thread thread = new Thread("IDE-ONE-" + finalI) {
                @Override
                public void run() {
                    //设置日期格式
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    // new Date()为获取当前系统时间，也可使用当前时间戳
                    String start = df.format(new Date());
                    if (redissonLock.addTryLock(RedissonConstant.LOCK_KEY, 3, 2)) {
                        System.out.println("------------获取锁情况;循环i:" + finalI + ",锁结果：" + true + "------------");
                        try {
                            // 执行方法休眠10S,方便看执行结果
                            Thread.sleep(5000);
                            String end = df.format(new Date());
                            System.out.println("任务执行完成;start:" + start + ",end：" + end);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            redissonLock.removeLock(RedissonConstant.LOCK_KEY);
                        }
                    } else {
                        System.out.println("------------获取锁情况;循环i:" + finalI + ",锁结果：" + false + "------------");
                        System.out.println("任务排队中，请勿重复操作！");
                    }
                }
            };
            thread.start();
            thread.join();
        }
    }

    public static void ss() {

    }


    //redisson 测试用例省略
}
