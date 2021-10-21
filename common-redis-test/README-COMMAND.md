##  Redis常用命令

Redis相关可执行文件的主要作用

    （1）redis-server  -------Redis服务器
    （2）redis-cli         -------Redis命令行客户端
    （3）redis-benchmark ---------Redis性能测试工具
    （4）redis-check-aof ----------AOF文件修复工具
    （5）redis-check-dump --------RDB文件检查工具

### Redis应用级

    启动服务命令： ./redis-server /usr/local/redis/redis-5.0.14/etc/redis.conf
    启动客户端命令： ./redis-cli
    停止服务：  ./redis-cli -p 6379 shutdown   (注：不要使用kill -9 PID,会导致备份丢数据)
    查看服务： ps aux|grep redis
    查看端口: netstat -antp | grep 6379

### Redis操作命令

####  开发常用

    查看全部key （不要在代码中使用）
    
        keys *
    
    匹配key
    
        keys apple*
    
    key的有效时间
    
        ttl key
            说明：
            key不存在返回-2
            key没设置过期时间返回-1
            key有设置过期时间，返回剩余时间。（单位毫秒）
    
    设置过期时间


#### 按数据类型



### 参考

    Redis官网 https://www.redis.net.cn/tutorial/3501.html || https://redis.io/download
    
    Redis源码地址：https://github.com/redis/redis

    Redis速度为什么这么快 https://blog.csdn.net/xlgen157387/article/details/79470556

    Redis介绍：https://blog.csdn.net/qq_44472134/article/details/104252693

    Redis持久化区别： https://www.cnblogs.com/shizhengwen/p/9283973.html