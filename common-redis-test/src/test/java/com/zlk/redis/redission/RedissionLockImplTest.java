package com.zlk.redis.redission;

import com.zlk.common.redis.redission.IRedissionLock;
import com.zlk.common.redis.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author likuan.zhou
 * @title: RedissionLockImplTest
 * @projectName common-test
 * @description: redission 测试类
 * @date 2021/10/9/009 9:12
 */
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
// 注 类和方法必须为public才能运行
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedissionLockImplTest {
    //过期时间
    private final static Long EXPIRE_TIME = 60*60L;
    //kye分割使用:会按文件夹方式分割归类。
    //字符串类型key（key:String）
    private final static String LOCK_KEY = "lock:key";

    @Autowired
    private IRedissionLock redissionLock;

    //===============================可重入锁（Reentrant Lock）=====================================
    //基于Redis的Redisson分布式可重入锁RLock Java对象实现了java.util.concurrent.locks.Lock接口。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口
    // 可重入锁: 避免死锁，可重复递归调用的锁,同一线程外层函数获取锁后,内层递归函数仍然可以获取锁,并且不发生死锁(前提是同一个对象或者class)

    @Test
    public void testAddLock() {
        //加可重入锁-默认30S
        //Redisson实例被关闭前，不断的延长锁的有效期。默认情况下，看门狗的检查锁的超时时间是30秒钟，也可以通过修改Config.lockWatchdogTimeout来另行指定。
        Boolean bl = redissionLock.addLock(LOCK_KEY);
        assertEquals(true,bl);
    }

    @Test
    public void testRemoveLock() {
        //手动释放可重入锁
        Boolean bl = redissionLock.removeLock(LOCK_KEY);
        assertEquals(true,bl);
    }
}
