##  RocketMQ命令

    Linux：conts7

    安装JDK8与Maven: RocketMQ java编写，需要java环境
        官网下载网页链接：https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

    Rocketmq版本: 5.0.14
        官网下载网页链接：https://redis.io/download
    
    Rocketmq开源项目(集群监控)：incubator-rocketmq-externals
        参考：https://www.jianshu.com/p/63f4062b661d

    mqadmin管理工具(命令操作)：
        进入RocketMQ安装位置，在bin目录下执行
        ./mqadmin {command} {args}

外网ip：47.119.180.152 

**启动与关闭**

    rocketmq/bin目录下

    1：启动命令：
        nohup sh mqnamesrv &
            查看日志命令：日志命令：tail -f ~/logs/rocketmqlogs/namesrv.log
        nohup sh mqbroker -n 47.119.180.152:9876 -c /usr/local/rocketmq/rocketmq-4.6.1/conf/broker.conf autoCreateTopicEnable=true &   （注：mq集群下自动创建top是有问题的autoCreateTopicEnable=true）
            查看日志命令：tail -f ~/logs/rocketmqlogs/broker.log
    
    2：关闭命令： 
        关闭namesrv服务：sh mqshutdown namesrv
        关闭broker服务 ：sh mqshutdown broker

**启动rocketmq-console**

    nohup java -jar rocketmq-console-ng-1.0.0.jar --rocketmq.config.namesrvAddr='47.119.180.152:9876' >out.log 2>&1 &

### 1 常用命令

#### 1.1 清空全部消息（谨慎使用）

#### 1.2 清空某个topic消息（谨慎使用）

进入rocketmq的bin目录下

    获取cluster列表：./mqadmin clusterList -n 127.0.0.1:9876 
        --find / -name '*ext*' |grep jdk
        --tools.sh 配置JAVA环境JAVA_OPT="${JAVA_OPT} -Djava.ext.dirs=${BASE_DIR}/lib:${JAVA_HOME}/jre/lib/ext:${JAVA_HOME}/lib/ext:/usr/local/jdk1.8.0_311/jre/lib/ext"

    查看topic列表： ./mqadmin topicList -n 127.0.0.1:9876

    清除当前机器下topic为clustering-topic7 ： ./mqadmin deleteTopic -c HOSTNAME -n 47.119.180.152:9876 -t clustering-topic7 

    说明：
    mqadmin deleteTopic -c <arg> [-h] [-n <arg>] -t <arg>
    -c,--clusterName <arg>   从哪个集群上删除topic
    -h,--help                打印帮助文档
    -n,--namesrvAddr <arg>   namesrv地址 例如: 192.168.0.1:9876;192.168.0.2:9876
    -t,--topic <arg>         待删除的topic名称，根据查询出来的删除或者指定的名称删除





### 参考


    Rocketmq源码(重点)：https://github.com/apache/rocketmq/tree/master/docs/cn
 
    Rocketmq官网：http://rocketmq.apache.org/
    
    Rocketmq文档：http://rocketmq.apache.org/docs/quick-start/

    安装教程（详细）https://www.freesion.com/article/2834981885/

    RocketMQ 阿里云部署 设置brokerIP 及不生效问题：https://blog.csdn.net/svncvs/article/details/88243728