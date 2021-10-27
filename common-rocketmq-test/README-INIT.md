##  Linux安装Rocketmq教程

    Linux：conts7

    安装JDK8: RocketMQ java编写，需要jdk环境
        官网下载网页链接：https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

    Rocketmq版本: 5.0.14
        官网下载网页链接：https://redis.io/download
    
    Rocketmq开源项目(集群监控)：incubator-rocketmq-externals
        参考：https://www.jianshu.com/p/63f4062b661d

    mqadmin管理工具(命令操作)：
        进入RocketMQ安装位置，在bin目录下执行
        ./mqadmin {command} {args}


### 1 Linux安装JDK8

linux位数：getconf LONG_BIT

JDK8官网下载网页链接：https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

![Image text](./images/jdk8下载地址.png)

    cd /usr/local

    Xshell上传：rz

    tar -zxvf  jdk-8u311-linux-aarch64.tar.gz

    修改环境变量
    vim /etc/profile
    export JAVA_HOME=/usr/local/jdk1.8.0_311
    export JRE_HOME=${JAVA_HOME}/jre
    export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib:$CLASSPATH
    export JAVA_PATH=${JAVA_HOME}/bin:${JRE_HOME}/bin
    export PATH=$PATH:${JAVA_PATH}

    刷新配置
    source /etc/profile

### 2 Rocketmq 下载安装

2.1 下载

http://rocketmq.apache.org/dowloading/releases/

![Image text](./images/rocketmq下载地址.png)


2.2 Xshell上传zip并解压

    切换目录：cd /usr/local

    创建rocketmq文件夹：mkdir rocketmq
     
    切换目录： cd rocketmq
    
    Xshell上传：rz 

    解压zip: unzip rocketmq-all-4.4.0-bin-release.zip

    更改解压目录名称： mv rocketmq-all-4.6.1-bin-release rocketmq-4.6.1

    cd /usr/local/rocketmq/rocketmq-4.6.1/bin

    启动参数:虚拟机内存不够，可能启动失败，可以更改内存。（服务器上不需要改）
    vi runserver.sh
         #JAVA_OPT="${JAVA_OPT} -server -Xms4g -Xmx4g -Xmn2g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"
         改为：
         #JAVA_OPT="${JAVA_OPT} -server -Xms128M -Xmx128M -Xmn128M -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128M"


    vi runbroker.sh
        #JAVA_OPT="${JAVA_OPT} -server -Xms8g -Xmx8g -Xmn4g"
        改为：
        JAVA_OPT="${JAVA_OPT} -server -Xms128M -Xmx128M -Xmn128M"


### 3 Rocketmq启动

Rocketmq配置文件：/usr/local/rocketmq/rocketmq-4.6.1/conf/broker.conf

3.1 bin目录下启动nameserve

    切换目录：cd /usr/local/rocketmq/rocketmq-4.6.1/bin

    启动nameserve： nohup sh mqnamesrv &
    
    启动是否成功的日志命令：tail -f ~/logs/rocketmqlogs/namesrv.log

3.2 bin目录下启动broker

    切换目录： cd /usr/local/rocketmq/rocketmq-4.6.1/bin

    选择配置文件启动broker： nohup sh mqbroker -c /usr/local/rocketmq/rocketmq-4.6.1/conf/broker.conf >/dev/null 2>&1 &

    netstat -ntlp

    查看日志命令：tail -f ~/logs/rocketmqlogs/broker.log
    查看启动状态：jps


### 参考


    Rocketmq源码(重点)：https://github.com/apache/rocketmq/tree/master/docs/cn
 
    Rocketmq官网：http://rocketmq.apache.org/
    
    Rocketmq文档：rocketmq文档地址：http://rocketmq.apache.org/docs/quick-start/

    安装教程：https://blog.csdn.net/darendu/article/details/103539968 
             https://blog.csdn.net/qq_41463655/article/details/101907665

