package com.zlk.redis.service;

import com.zlk.common.redis.bloom.BloomFilterHelper;
import com.zlk.common.redis.bloom.RedisBloomFilter;
import com.zlk.core.model.po.User;
import com.zlk.redis.constant.Constant;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author likuan.zhou
 * @title: RedissonLockController
 * @projectName common-test
 * @description:
 *            Redis缓存穿透：数据库与缓存中都不存在，黑客大量访问打到数据库；（布隆过滤器/返回空对象）
 *            Redis缓存击穿：数据库中存在对应值，redi缓存过期，大量请求访问打到数据库；（分布式锁Redisson）
 *            Redis缓存雪崩：缓存大面积失效，或者重启消耗大量资源。（缓存时间设置随机，启动加到队列,冷热数据分离，预加载等，热数据均匀分布到不同缓存数据库）
 * @date 2021/10/18/002 13:40
 */
@Service
public class CachePenetrationService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redisson;
    @Autowired
    private BloomFilterHelper bloomFilterHelper;
    @Autowired
    private RedisBloomFilter redisBloomFilter;

    /**  (该处为了方便理解，未使用个人封装的redis方法)
     *   Redis缓存穿透：数据库与缓存中都不存在，黑客大量访问打到数据库；（布隆过滤器/返回空对象）
     *   Redis缓存击穿：数据库中存在对应值，redi缓存过期，大量请求访问打到数据库；（分布式锁Redisson）
     *   Redis缓存雪崩：缓存大面积失效，或者重启消耗大量资源。（缓存时间设置随机，启动加到队列,冷热数据分离，预加载等，热数据均匀分布到不同缓存数据库）
     *   @param id 用户id
     *   @return 用户
     */
    public User getUserList(String id){
        //布隆过滤器判断key不存在直接返回（解决Redis缓存穿透）
        // setnx+hash (注：初始时候需要将key全部hash后setnx到redis)
        boolean bloomFilter = redisBloomFilter.includeByBloomFilter(bloomFilterHelper,  Constant.USER_BLOOM_KEY+":"+id, id);
        if(!bloomFilter){
            //key不存在（没有数据库对应数据）。缓存穿透，直接返回。
            System.out.println("缓存穿透,直接返回");
            return null;
        }
        //key设置为user_list,测试代码不考虑分页
        User user = (User)redisTemplate.opsForValue().get(Constant.USER_KEY+":"+id);
        if (user!=null){
            System.out.println("缓存中有对应值");
            return user;
        }

        //分布式锁，redisson保证缓存的分布式锁不过期。（解决Redis缓存击穿）
        RLock lock = redisson.getLock(Constant.USER_LOCK_KEY+":"+id);
        try{
            //阻塞，设置时间。保证redis中可以解锁。（注：设置过期时间到期会直接解锁，看门狗失效）
            //lock.lock(30,TimeUnit.SECONDS);

            //再查询一次缓存，当第一次入缓存成功后，第一个线程以缓存。不再查询数据库。
            user = (User)redisTemplate.opsForValue().get(Constant.USER_KEY+":"+id);
            if (user!=null){
                System.out.println("缓存中有对应值");
                return user;
            }
            //缓存中不存在，查寻数据库(伪代码)
            // user = userDao.getUserById1();
            System.out.println("数据库中有对应值，缓存中过期，查询数据库");
            //设置时间30分钟失效，将数据写到redis。
            redisTemplate.opsForValue().set(id,user,30, TimeUnit.SECONDS);
            // 如果是一个批量写入缓存，如刷新缓存的方法，则可以设置随机的过期时间。（解决Redis缓存雪崩)
        }finally {
            //保证解锁成功
            lock.unlock();
        }
        return user;
    }

    // 已过时，会有容错误判（和数组长度与hash个数有关，每个hash定位到的位置标记为1）。hash会出现碰撞，内存有共享。就是牺牲准确率换取速度与内存。
    // private static BloomBuilder<Integer> blooFilter = BloomBuilder.create(Funnels.integerFunnel(),10000000,0.01);



}
