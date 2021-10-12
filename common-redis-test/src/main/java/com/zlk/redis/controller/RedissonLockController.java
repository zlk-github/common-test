package com.zlk.redis.controller;

import com.zlk.common.redis.redisson.IRedissonLock;
import com.zlk.redis.constant.RedissonConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author likuan.zhou
 * @title: RedissonLockController
 * @projectName common-test
 * @description: Redisson锁
 * @date 2021/10/9/009 13:40
 */
@Api(tags = "Redisson锁")
@RestController
@RequestMapping("redisson")
public class RedissonLockController {
    // 加锁方法不要使用测试用例测试，要不然方法执行结束相当于宕机，无法为锁续命（到期就会自动失效）。
    // swagger : http://localhost:8015/swagger-ui.html
    // 每次测试前清除锁再测试removeLock（）;

    @Autowired
    private IRedissonLock redissonLock;

    //===============================可重入锁（Reentrant Lock）=====================================
    // 基于Redis的Redisson分布式可重入锁RLock Java对象实现了java.util.concurrent.locks.Lock接口。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口
    // 可重入锁: 避免死锁，可重复递归调用的锁,同一线程外层函数获取锁后,内层递归函数仍然可以获取锁,并且不发生死锁(前提是同一个对象或者class)
    // 设置超时类型，看门狗会失效.

    @PostMapping("/addLock")
    @ApiOperation("加可重入锁")
    public String addLock(){
        //加可重入锁-默认30S
        //Redisson实例被关闭前，不断的延长锁的有效期。默认情况下，看门狗的检查锁的超时时间是30秒钟，也可以通过修改Config.lockWatchdogTimeout来另行指定。
        Boolean bl = redissonLock.addLock(RedissonConstant.LOCK_KEY);
        //注：释放锁需要手动调用 lock.unlock();或者宕机后到期自动失效。
        //redissonLock.removeLock(RedissonConstant.LOCK_KEY);
        return "加锁成功";
    }

    @PostMapping("/removeLock")
    @ApiOperation("删除可重入锁")
    public String removeLock(){
        //解锁(手动设置失效时间的不能调用该方法)
        Boolean bl = redissonLock.removeLock(RedissonConstant.LOCK_KEY);
        return "解锁成功";
    }

    @PostMapping("/addLockTime")
    @ApiOperation("加可重入锁--设置失效时间（看门狗失效）")
    public String addLockTime(){
        //加可重入锁-设置失效时间
        //指定失效时间（到期会自动失效），无需调用unlock方法手动解锁
        Boolean bl = redissonLock.addLock(RedissonConstant.LOCK_KEY,60);
        //注：自动到期失效;或者宕机后到期自动失效,也可以手动失效
        //redissonLock.removeLock(RedissonConstant.LOCK_KEY);
        return "加锁成功";
    }

    @PostMapping("/addTryLock")
    @ApiOperation("可重入锁--过期（看门狗失效）--等待类型")
    public String addTryLock(){
        // 使用jmeter压线程进行测试（100线程循环10次）
        // 尝试加锁，最多等待waitTime秒，上锁以后time秒自动解锁。超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
        // 说明：尝试加锁的线程等待waitTime（100）秒（超过时间未获取到锁返回false.否则返回true），超时执行下面逻辑。获取到锁的线程持有锁time（10）秒后锁失效。
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // new Date()为获取当前系统时间，也可使用当前时间戳
        String start = df.format(new Date());
        // 锁获取等待时间100s,锁失效时间10S
        if (redissonLock.addTryLock(RedissonConstant.LOCK_KEY, 100, 10)) {
            System.out.println("------------获取锁情况,锁结果：" + true + "------------");
            try {
                // 执行方法休眠5S,方便看执行结果
                Thread.sleep(5000);
                String end = df.format(new Date());
                System.out.println("任务执行完成;start:" + start + ",end：" + end);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 手动解锁，不手动解则到期失效
                redissonLock.removeLock(RedissonConstant.LOCK_KEY);
            }
        } else {
            System.out.println("------------获取锁情况,锁结果：" + false + "------------");
            System.out.println("任务排队中，请勿重复操作！");
        }
        //注：自动到期失效;或者宕机后到期自动失效；不需要调用unlock失效。

       /* 结果如下
        ------------获取锁情况,锁结果：true------------
        任务执行完成;start:2021-10-11 15:50:21,end：2021-10-11 15:50:26
        ------------获取锁情况,锁结果：true------------
        任务执行完成;start:2021-10-11 15:50:26,end：2021-10-11 15:50:31
        ------------获取锁情况,锁结果：true------------
        任务执行完成;start:2021-10-11 15:50:31,end：2021-10-11 15:50:36
        。。。。。。。。。
        ------------获取锁情况,锁结果：false------------
        任务排队中，请勿重复操作！
        ------------获取锁情况,锁结果：false------------
        任务排队中，请勿重复操作！
        任务执行完成;start:2021-10-11 15:50:21,end：2021-10-11 15:52:01
                ------------获取锁情况,锁结果：true------------
                ------------获取锁情况,锁结果：false------------
        任务排队中，请勿重复操作！*/
        return "加锁成功";
    }

    @PostMapping("/addTryLockAsync")
    @ApiOperation("可重入锁--过期（看门狗失效）--等待--异步类型")
    public String addTryLockAsync(){
        // 使用jmeter压线程进行测试（100线程循环10次）
        // 尝试加锁，最多等待waitTime秒，上锁以后time秒自动解锁。超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。异步执行
        // 说明：尝试加锁的线程等待waitTime（100）秒（超过时间未获取到锁返回false.否则返回true），超时执行下面逻辑。获取到锁的线程持有锁time（10）秒后锁失效。
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // new Date()为获取当前系统时间，也可使用当前时间戳
        String start = df.format(new Date());
        // 锁获取等待时间100s,锁失效时间10S
        Future<Boolean> future = redissonLock.addTryLockAsync(RedissonConstant.LOCK_KEY, 100, 10);

        System.out.println("------------获取锁情况,锁结果：" + true + "------------");
        try {
            Boolean bl = future.get();
            if (bl) {
                // 执行方法休眠5S,方便看执行结果
                try {
                    Thread.sleep(5000);
                    String end = df.format(new Date());
                    System.out.println("任务执行完成;start:" + start + ",end：" + end);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    // 手动解锁，不手动解则到期失效
                    redissonLock.removeLock(RedissonConstant.LOCK_KEY);
                }
            } else  {
                System.out.println("------------获取锁情况,锁结果：" + false + "------------");
                System.out.println("任务排队中，请勿重复操作！");
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        //注：自动到期失效;或者宕机后到期自动失效；不需要调用unlock失效。
       /* ------------获取锁情况,锁结果：true------------
        ------------获取锁情况,锁结果：true------------
        ------------获取锁情况,锁结果：true------------
        ------------获取锁情况,锁结果：true------------
        ------------获取锁情况,锁结果：false------------
        ------------获取锁情况,锁结果：false------------
        任务排队中，请勿重复操作！
        ------------获取锁情况,锁结果：false------------
        任务排队中，请勿重复操作！
        ------------获取锁情况,锁结果：false------------*/
        return "加锁成功";
    }






}
