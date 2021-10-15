package com.zlk.redis.controller;

import com.zlk.common.redis.redisson.IRedissonLock;
import com.zlk.redis.constant.RedissonConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
 * @description: Redisson分布式锁测试接口
 * @date 2021/10/9/009 13:40
 */
@Api(tags = "Redisson锁")
@RestController
@RequestMapping("redisson")
public class RedissonLockController {
    // 注：换其他锁测试时，手动清除一下之前的锁。为了测试方便没有取太多redis键key
    // 注：加锁方法不要使用测试用例测试，要不然方法执行结束相当于宕机，无法为锁续命（到期就会自动失效）。
    // swagger : http://localhost:8015/swagger-ui.html
    // 每次测试前清除锁再测试removeLock（）;
    // 设置日期格式
    final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private IRedissonLock redissonLock;

    @Autowired
    private RedissonClient redisson;

    //===============================可重入锁（Reentrant Lock）=====================================
    // 基于Redis的Redisson分布式可重入锁RLock Java对象实现了java.util.concurrent.locks.Lock接口。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口
    // 可重入锁: 避免死锁，可重复递归调用的锁,同一线程外层函数获取锁后,内层递归函数仍然可以获取锁,并且不发生死锁(前提是同一个对象或者class)
    // 设置超时类型，看门狗会失效.

    @PostMapping("/addLock")
    @ApiOperation("加可重入锁")
    public String addLock() {
        //加可重入锁-默认30S
        //Redisson实例被关闭前，不断的延长锁的有效期。默认情况下，看门狗的检查锁的超时时间是30秒钟，也可以通过修改Config.lockWatchdogTimeout来另行指定。
        Boolean bl = redissonLock.addLock(RedissonConstant.LOCK_KEY);
        //注：释放锁需要手动调用 lock.unlock();(宕机后到期会自动失效)。
        //redissonLock.removeLock(RedissonConstant.LOCK_KEY);
        return "加锁成功";
    }

    @PostMapping("/removeLock")
    @ApiOperation("删除可重入锁")
    public String removeLock() {
        //解锁(手动设置失效时间的不能调用该方法)
        Boolean bl = redissonLock.removeLock(RedissonConstant.LOCK_KEY);
        return "解锁成功";
    }

    @PostMapping("/addLockTime")
    @ApiOperation("加可重入锁--设置失效时间（看门狗失效）")
    public String addLockTime() {
        //加可重入锁-设置失效时间
        //指定失效时间（到期会自动失效），无需调用unlock方法手动解锁
        Boolean bl = redissonLock.addLock(RedissonConstant.LOCK_KEY,60);
        //注：自动到期失效;或者宕机后到期自动失效,也可以手动失效
        //redissonLock.removeLock(RedissonConstant.LOCK_KEY);
        return "加锁成功";
    }

    @PostMapping("/addTryLock")
    @ApiOperation("可重入锁--过期（看门狗失效）--等待类型")
    public String addTryLock() {
        // 使用jmeter压线程进行测试（100线程循环10次）
        // 尝试加锁，最多等待waitTime秒，上锁以后time秒自动解锁。超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
        // 说明：尝试加锁的线程等待waitTime（100）秒（超过时间未获取到锁返回false.否则返回true），超时执行下面逻辑。获取到锁的线程持有锁time（10）秒后锁失效。

        // new Date()为获取当前系统时间，也可使用当前时间戳
        String start = DATE_FORMAT.format(new Date());
        // 锁获取等待时间100s,锁失效时间10S
        if (redissonLock.addTryLock(RedissonConstant.LOCK_KEY, 100, 10)) {
            System.out.println("------------获取锁情况,锁结果：" + true + "------------");
            try {
                // 执行方法休眠5S,方便看执行结果
                Thread.sleep(5000);
                String end = DATE_FORMAT.format(new Date());
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
    public String addTryLockAsync() {
        // 使用jmeter压线程进行测试（100线程循环10次）
        // 尝试加锁，最多等待waitTime秒，上锁以后time秒自动解锁。超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。异步执行
        // 说明：尝试加锁的线程等待waitTime（100）秒（超过时间未获取到锁返回false.否则返回true），超时执行下面逻辑。获取到锁的线程持有锁time（10）秒后锁失效。

        // new Date()为获取当前系统时间，也可使用当前时间戳
        String start = DATE_FORMAT.format(new Date());
        // 锁获取等待时间100s,锁失效时间10S
        Future<Boolean> future = redissonLock.addTryLockAsync(RedissonConstant.LOCK_KEY, 100, 10);

        System.out.println("------------获取锁情况,锁结果：" + true + "------------");
        try {
            Boolean bl = future.get();
            if (bl) {
                // 执行方法休眠5S,方便看执行结果
                try {
                    Thread.sleep(5000);
                    String end = DATE_FORMAT.format(new Date());
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

    //=============================== 公平锁（Fair Lock）=====================================
    // 基于Redis的Redisson分布式可重入公平锁也是实现了java.util.concurrent.locks.Lock接口的一种RLock对象。。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口
    // 它保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。所有请求线程会在一个队列中排队，
    // 当某个线程出现宕机时，Redisson会等待5秒后继续下一个线程，也就是说如果前面有5个线程都处于等待状态，那么后面的线程会等待至少25秒。
    // 以下将不在赘述自定义过期，等待与异步锁。

    @PostMapping("/addFairLock")
    @ApiOperation("重入锁--公平锁--普通类型")
    public String addFairLock() {
        // 使用jmeter压线程进行测试（100线程循环2次）

        //它保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。所有请求线程会在一个队列中排队，
        // 当某个线程出现宕机时，Redisson会等待5秒后继续下一个线程，也就是说如果前面有5个线程都处于等待状态，那么后面的线程会等待至少25秒。

        //加可重入锁-默认30S,到期看门狗会重置时间
        //Redisson实例被关闭前，不断的延长锁的有效期。默认情况下，看门狗的检查锁的超时时间是30秒钟，也可以通过修改Config.lockWatchdogTimeout来另行指定。
        //公平锁讲究先进先出原则。

        // new Date()为获取当前系统时间，也可使用当前时间戳
        String start = DATE_FORMAT.format(new Date());
        Boolean bl = redissonLock.addFairLock(RedissonConstant.LOCK_KEY);
        try {
            Thread.sleep(3000);
            String end = DATE_FORMAT.format(new Date());
            System.out.println("任务执行完成;start:" + start + ",end：" + end);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //注：释放锁需要手动调用 lock.unlock();(宕机后到期会自动失效)。
            redissonLock.removeFairLock(RedissonConstant.LOCK_KEY);
        }
       /* lock:key为redis的key
        会生成一个key唯一锁(lock:key)，一个线程排队队列(redisson_lock_queue:{lock:key})，一个zset (redisson_lock_timeout:{lock:key})
        线程进入开始时间一样，结束时间差3S(方法执行)。说明进入了排队执行。
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:17
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:20
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:23
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:26
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:29
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:32
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:35
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:38
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:41
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:44
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:47
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:50
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:53
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:56
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:42:59
        任务执行完成;start:2021-10-12 09:41:57,end：2021-10-12 09:43:02*/
        return "加锁成功";
    }

    @PostMapping("/removeFairLock")
    @ApiOperation("释放重入锁--公平锁")
    public String removeFairLock() {
        Boolean bl = redissonLock.removeFairLock(RedissonConstant.LOCK_KEY);
        return "释放重入锁--公平锁";
    }

    //=============================== 联锁（MultiLock）=====================================
    // 基于Redis的Redisson分布式联锁RedissonMultiLock对象可以将多个RLock对象关联为一个联锁，每个RLock对象实例可以来自于不同的Redisson实例。
    // 联锁: 保证同时加锁，且所有锁都需要上锁成功才算成功（里面具体的锁可以自己选择，然后做实现。下面以普通可重入锁来实现）
    @PostMapping("/addMultiLock")
    @ApiOperation("联锁--可重入--普通类型")
    public String addMultiLock() {
        RLock rLock1 = redisson.getLock(RedissonConstant.LOCK_KEY + 1);
        RLock rLock2 = redisson.getLock(RedissonConstant.LOCK_KEY + 2);
        RLock rLock3 = redisson.getLock(RedissonConstant.LOCK_KEY + 3);
        RLock rLock4 = redisson.getLock(RedissonConstant.LOCK_KEY + 4);
        RLock rLock5 = redisson.getLock(RedissonConstant.LOCK_KEY + 5);
        //联锁: 保证同时加锁，且所有锁都需要上锁成功才算成功（里面具体的锁可以自己选择，然后做实现。下面以普通可重入锁来实现）
        Boolean bl = this.redissonLock.addMultiLock(rLock1,rLock2,rLock3,rLock4,rLock5);
        //释放锁
        //redissonLock.removeMultiLock(rLock1,rLock2,rLock3,rLock4,rLock5);
        return "加联锁--可重入--普通类型";
    }

    @PostMapping("/removeMultiLock")
    @ApiOperation("释放联锁--可重入--普通类型")
    public String removeMultiLock() {
        RLock rLock1 = redisson.getLock(RedissonConstant.LOCK_KEY + 1);
        RLock rLock2 = redisson.getLock(RedissonConstant.LOCK_KEY + 2);
        RLock rLock3 = redisson.getLock(RedissonConstant.LOCK_KEY + 3);
        RLock rLock4 = redisson.getLock(RedissonConstant.LOCK_KEY + 4);
        RLock rLock5 = redisson.getLock(RedissonConstant.LOCK_KEY + 5);
        Boolean bl = this.redissonLock.removeMultiLock(rLock1,rLock2,rLock3,rLock4,rLock5);
        //释放锁
        //redissonLock.removeMultiLock(rLock1,rLock2,rLock3,rLock4,rLock5);
        return "释放联锁--可重入--普通类型";
    }

    //=============================== 红锁（RedLock）=====================================
    // 基于Redis的Redisson红锁RedissonRedLock对象实现了Redlock介绍的加锁算法。该对象也可以用来将多个RLock对象关联为一个红锁，每个RLock对象实例可以来自于不同的Redisson实例。
    // 红锁: 同时加锁，大部分锁节点加锁成功（最少locks.size() / 2 + 1为成功）就算成功。
    @PostMapping("/addRedLock")
    @ApiOperation("红锁--可重入--普通类型")
    public String addRedLock() {
        RLock rLock1 = redisson.getLock(RedissonConstant.LOCK_KEY + 1);
        RLock rLock2 = redisson.getLock(RedissonConstant.LOCK_KEY + 2);
        RLock rLock3 = redisson.getLock(RedissonConstant.LOCK_KEY + 3);
        RLock rLock4 = redisson.getLock(RedissonConstant.LOCK_KEY + 4);
        RLock rLock5 = redisson.getLock(RedissonConstant.LOCK_KEY + 5);
        Boolean bl = this.redissonLock.addRedLock(rLock1,rLock2,rLock3,rLock4,rLock5);
        //释放锁
        //redissonLock.removeRedLock(rLock1,rLock2,rLock3,rLock4,rLock5);
        return "加红锁--可重入--普通类型";
    }

    @PostMapping("/removeRedLock")
    @ApiOperation("红锁--可重入--普通类型")
    public String removeRedLock() {
        RLock rLock1 = redisson.getLock(RedissonConstant.LOCK_KEY + 1);
        RLock rLock2 = redisson.getLock(RedissonConstant.LOCK_KEY + 2);
        RLock rLock3 = redisson.getLock(RedissonConstant.LOCK_KEY + 3);
        RLock rLock4 = redisson.getLock(RedissonConstant.LOCK_KEY + 4);
        RLock rLock5 = redisson.getLock(RedissonConstant.LOCK_KEY + 5);
        Boolean bl = this.redissonLock.removeRedLock(rLock1,rLock2,rLock3,rLock4,rLock5);
        return "释放红锁--可重入--普通类型";
    }

    //===============================读写锁（ReadWriteLock）=====================================
    // 基于Redis的Redisson分布式可重入读写锁RReadWriteLock Java对象实现了java.util.concurrent.locks.ReadWriteLock接口。其中读锁和写锁都继承了RLock接口。
    // 读写锁: 分布式可重入读写锁允许同时有多个读锁和一个写锁处于加锁状态。
    // 读锁使用共享模式；写锁使用独占模式。
    // 以下测试读写锁的：读读共享，读写互斥，写写互斥

    @PostMapping("/addRrLock")
    @ApiOperation("读写锁--可重入--普通类型--读读共享")
    public String addRrLock() {
        //测试读读共享
        for (int i = 0; i < 2; i++) {
            new Thread() {
                @Override
                public void run() {
                    getRLock(RedissonConstant.LOCK_KEY,this);
                }
            }.start();
        }
        /*
        --读写锁，读读共享（线程1和线程2共用锁，数据交叉执行）
        key:lock:key,线程名称：Thread-15读操作任务开始时间：2021-10-13 10:08:29
        key:lock:key,线程名称：Thread-16读操作任务开始时间：2021-10-13 10:08:29
        key:lock:key,线程名称：Thread-16,正在进行读操作……
        key:lock:key,线程名称：Thread-15,正在进行读操作……
        key:lock:key,线程名称：Thread-15,正在进行读操作……
        key:lock:key,线程名称：Thread-16,正在进行读操作……
        key:lock:key,线程名称：Thread-15,正在进行读操作……
        key:lock:key,线程名称：Thread-16,正在进行读操作……
        key:lock:key,线程名称：Thread-16,正在进行读操作……
        key:lock:key,线程名称：Thread-15,正在进行读操作……
        key:lock:key,线程名称：Thread-16,正在进行读操作……
        key:lock:key,线程名称：Thread-15,正在进行读操作……
        key:lock:key,线程名称：Thread-15,读操作完毕！
        key:lock:key,线程名称：Thread-16,读操作完毕！
        key:lock:key,线程名称：Thread-15读操作任务结束时间：2021-10-13 10:08:29
        ==============================================================
        key:lock:key,线程名称：Thread-16读操作任务结束时间：2021-10-13 10:08:29
        ==============================================================*/
        return "读写锁--可重入--普通类型--读读共享";
    }

    private void getRLock(String key,Thread thread) {
        //读锁
        this.redissonLock.addRwrLock(key);

        // new Date()为获取当前系统时间，也可使用当前时间戳
        String start = DATE_FORMAT.format(new Date());
        System.out.println("key:"+key+",线程名称："+thread.getName()+"读操作任务开始时间：" + start);
        for (int i = 0; i < 2; i++) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("key:"+key+",线程名称："+thread.getName()+",正在进行读操作……");
        }
        System.out.println("key:"+key+",线程名称："+thread.getName()+",读操作完毕！");
        String end = DATE_FORMAT.format(new Date());
        System.out.println("key:"+key+",线程名称："+thread.getName()+"读操作任务结束时间：" + end);
        System.out.println("==============================================================");
        //释放读锁
        redissonLock.removeRwrLock(key);
    }

    @PostMapping("/addRwLock")
    @ApiOperation("读写锁--可重入--普通类型--读写互斥")
    public String addRwLock() {
        //测试读写互斥
        //读
        new Thread() {
            @Override
            public void run() {
                getRLock(RedissonConstant.LOCK_KEY,this);
            }
        }.start();

        // 写
        new Thread() {
            @Override
            public void run() {
                getWLock(RedissonConstant.LOCK_KEY,this);
            }
        }.start();
        /*
        --读写锁，读写互斥
        key:lock:key,线程名称：Thread-14写操作任务开始时间：2021-10-13 10:07:04
        key:lock:key,线程名称：Thread-14,正在进行写操作……
        key:lock:key,线程名称：Thread-14,正在进行写操作……
        key:lock:key,线程名称：Thread-14,正在进行写操作……
        key:lock:key,线程名称：Thread-14,正在进行写操作……
        key:lock:key,线程名称：Thread-14,正在进行写操作……
        key:lock:key,线程名称：Thread-14,写操作完毕！
        key:lock:key,线程名称：Thread-14写操作任务结束时间：2021-10-13 10:07:04
        ==============================================================
        key:lock:key,线程名称：Thread-13读操作任务开始时间：2021-10-13 10:07:04
        key:lock:key,线程名称：Thread-13,正在进行读操作……
        key:lock:key,线程名称：Thread-13,正在进行读操作……
        key:lock:key,线程名称：Thread-13,正在进行读操作……
        key:lock:key,线程名称：Thread-13,正在进行读操作……
        key:lock:key,线程名称：Thread-13,正在进行读操作……
        key:lock:key,线程名称：Thread-13,读操作完毕！
        key:lock:key,线程名称：Thread-13读操作任务结束时间：2021-10-13 10:07:04
        ==============================================================*/
        return "读写锁--可重入--普通类型--读读共享";
    }

    private void getWLock(String key,Thread thread) {
        //写锁
        this.redissonLock.addRwwLock(key);

        // new Date()为获取当前系统时间，也可使用当前时间戳
        String start = DATE_FORMAT.format(new Date());
        System.out.println("key:"+key+",线程名称："+thread.getName()+"写操作任务开始时间：" + start);
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("key:"+key+",线程名称："+thread.getName()+",正在进行写操作……");
        }
        System.out.println("key:"+key+",线程名称："+thread.getName()+",写操作完毕！");
        String end = DATE_FORMAT.format(new Date());
        System.out.println("key:"+key+",线程名称："+thread.getName()+"写操作任务结束时间：" + end);
        System.out.println("==============================================================");
        //释放写锁
        redissonLock.removeRwwLock(key);
    }

    @PostMapping("/addWwLock")
    @ApiOperation("读写锁--可重入--普通类型--写写互斥")
    public String addWwLock() {
        //测试写写互斥
        for (int i = 0; i < 5; i++) {
            // 写
            new Thread() {
                @Override
                public void run() {
                    getWLock(RedissonConstant.LOCK_KEY,this);
                }
            }.start();
        }
        /*
        --读写锁，写写互斥
        key:lock:key,线程名称：Thread-14写操作任务开始时间：2021-10-14 08:30:18
        key:lock:key,线程名称：Thread-14,正在进行写操作……
        key:lock:key,线程名称：Thread-14,正在进行写操作……
        key:lock:key,线程名称：Thread-14,正在进行写操作……
        key:lock:key,线程名称：Thread-14,正在进行写操作……
        key:lock:key,线程名称：Thread-14,正在进行写操作……
        key:lock:key,线程名称：Thread-14,写操作完毕！
        key:lock:key,线程名称：Thread-14写操作任务结束时间：2021-10-14 08:30:18
        ==============================================================
        key:lock:key,线程名称：Thread-13写操作任务开始时间：2021-10-14 08:30:18
        key:lock:key,线程名称：Thread-13,正在进行写操作……
        key:lock:key,线程名称：Thread-13,正在进行写操作……
        key:lock:key,线程名称：Thread-13,正在进行写操作……
        key:lock:key,线程名称：Thread-13,正在进行写操作……
        key:lock:key,线程名称：Thread-13,正在进行写操作……
        key:lock:key,线程名称：Thread-13,写操作完毕！
        key:lock:key,线程名称：Thread-13写操作任务结束时间：2021-10-14 08:30:19
        ==============================================================
         */
        return "读写锁--可重入--普通类型--读读共享";
    }

    //===============================信号量（Semaphore 计数信号量）=====================================
    // 基于Redis的Redisson的分布式信号量（Semaphore）Java对象RSemaphore采用了与java.util.concurrent.Semaphore相似的接口和用法。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。
    // 信号量: 当你的服务最大只能满足每秒1w的并发量时，我们可以使用信号量进行限流，当访问的请求超过1w时会进行等待（阻塞式或者非阻塞式，根据业务需求）。
    // --如停车位，进出场地（车位有限）。唯一计数。

    @PostMapping("/trySetPermits")
    @ApiOperation("初始化信号量值(停车场总车位)")
    public String trySetPermits() {
        //初始化信号量值为6。只能初始化一次（下次需要初始需要删除key,否则trySetPermits不能再重置值）
        this.redissonLock.trySetPermits(RedissonConstant.LOCK_KEY,6);
        return "初始化信号量值";
    }

    @PostMapping("/useSemaphore")
    @ApiOperation("信号量使用（占用车位，空车位减少）")
    public String useSemaphore() {
        //每调用一次，信号量会减一，如果为0则一直等待，直到信号量>0
        this.redissonLock.useSemaphore(RedissonConstant.LOCK_KEY);
        //每调用一次，信号量会减2，如果为0则一直等待，直到信号量>0
        //this.redissonLock.useSemaphore(RedissonConstant.LOCK_KEY,2);
        System.out.println("抢占车位成功");
        /**
         输出结果（信号量初始6，中途不释放信号量，请求10次，只有6辆车可以抢到车位。其余进入等待）
         抢占车位成功
         抢占车位成功
         抢占车位成功
         抢占车位成功
         抢占车位成功
         抢占车位成功
         */
        return "信号量使用";
    }

    @PostMapping("/releaseSemaphore")
    @ApiOperation("释放信号量（空出车位，空车位增加）")
    public String releaseSemaphore() {
        //这里会将信号量里面的值+1，也就是释放信号量
        this.redissonLock.releaseSemaphore(RedissonConstant.LOCK_KEY);
        //这里会将信号量里面的值+2，也就是释放信号量
        //this.redissonLock.releaseSemaphore(RedissonConstant.LOCK_KEY,2);
        System.out.println("释放车位成功");
        /**
         输出结果（信号量初始6，中途不占用信号量，请求10次，释放了10次（超出车位，需要做限制））
             释放车位成功
             释放车位成功
             释放车位成功
             释放车位成功
             释放车位成功
             释放车位成功
             释放车位成功
             释放车位成功
             释放车位成功
             释放车位成功
         */

        // 伪代码
        // 注： 实际使用中需要控制释放总数量不要大于信号量初始值（如停车场车位有限，空车位不能大于总车位）
        // 当前信号量数
        // int permits = redisson.getSemaphore(RedissonConstant.LOCK_KEY).drainPermits();
        // if(（permits+释放值）<信号量初始值) {//释放信号量}
        return "释放信号量";
    }

    //===============================可过期性信号量（PermitExpirableSemaphore）=====================================
    // 基于Redis的Redisson可过期性信号量（PermitExpirableSemaphore）是在RSemaphore对象的基础上，为每个信号增加了一个过期时间。每个信号可以通过独立的ID来辨识，释放时只能通过提交这个ID才能释放。它提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。
    // 可过期性信号量: ID来辨识的信号量设置有效时间
    // 和不过期信号量相比，可过期性信号量有持有时间限制。该处不赘述。


    //=============================== 闭锁（CountDownLatch）=====================================
    // 基于Redisson的Redisson分布式闭锁（CountDownLatch）Java对象RCountDownLatch采用了与java.util.concurrent.CountDownLatch相似的接口和用法。
    // 闭锁: 计数清零时执行（如班上20个同学全部离开才能锁教室门）
    @PostMapping("/trySetCount")
    @ApiOperation("初始化闭锁值（教室总人数）")
    public String trySetCount() {
        //初始化闭锁值为20
        this.redissonLock.trySetCount(RedissonConstant.LOCK_KEY,20);
        return "初始化闭锁值（教室总人数）";
    }

    @PostMapping("/removeCountDownLatch")
    @ApiOperation("闭锁值-1（教室离开一人）")
    public String removeCountDownLatch() {
        //闭锁值-1（教室离开一人）
        this.redissonLock.removeCountDownLatch(RedissonConstant.LOCK_KEY);
        System.out.println("有人提着书包跑路了");

        /*
        //先执行trySetCount初始20人，执行addCountDownLatch(锁门方法)无输出。
        //当removeCountDownLatch执行20次后，addCountDownLatch方法输出（人跑完了，关闭教室门回家。）
        //结果如下：
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        有人提着书包跑路了
        人跑完了，关闭教室门回家。*/
        return "教室离开一人";
    }

    @PostMapping("/addCountDownLatch")
    @ApiOperation("闭锁（关门教室门，上锁）")
    public String addCountDownLatch() {
        // 闭锁（关门上锁）
        // 当闭锁设置的初始值全部释放（调removeCountDownLatch方法使trySetCount清空为0），才往下执行，否则等待。
        this.redissonLock.addCountDownLatch(RedissonConstant.LOCK_KEY);
        System.out.println("人跑完了，关闭教室门回家。");
        return "关门上锁，拉闸断电！";
    }

}
