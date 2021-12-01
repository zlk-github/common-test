package com.zlk.producer.controller;


import com.zlk.producer.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生产者者事务监听器(事务消息2) 每个事务相当于一个业务处理方法
 * @author likuan.zhou
 * @date 2021/11/1/001 8:33
 */

@Slf4j
@Component
// 默认监控rocketMQTemplate.sendMessageInTransaction
//@RocketMQTransactionListener
//@RocketMQTransactionListener(rocketMQTemplateBeanName = "extRocketMQTemplate2")
public class ProducerTransactionListener2 implements RocketMQLocalTransactionListener{
    private AtomicInteger transactionIndex = new AtomicInteger(0);
    // 该记录
    private ConcurrentHashMap<Integer, Boolean> localTransMap=new ConcurrentHashMap<>();
    @Autowired
    private OrderService orderService;


    //executeLocalTransaction 方法来执行本地事务
    @Override
    @Transactional
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {

      /* 事务消息共有三种状态，提交状态、回滚状态、中间状态：
        TransactionStatus.CommitTransaction: 提交事务，它允许消费者消费此消息。
        TransactionStatus.RollbackTransaction: 回滚事务，它代表该消息将被删除，不允许被消费。
        TransactionStatus.Unknown: 中间状态，它代表需要检查消息队列来确定状态。*/
        //String msg = new String((byte[]) message.getPayload(), StandardCharsets.UTF_8);
        /*int value = transactionIndex.getAndIncrement();
        int status = value % 3;
        localTransMap.put(message.hashCode(),status);
        int i  = 1 / 0;
        log.info("执行本地事务，message:{},arg:{},status:{}",message,o,status);
        return  RocketMQLocalTransactionState.UNKNOWN;*/

        RocketMQLocalTransactionState  result;
        // 中间状态，它代表需要检查消息队列来确定状态。
        result = RocketMQLocalTransactionState.UNKNOWN;
        log.info("本地事务中间状态UNKNOWN，message:{},arg:{}",message,o);
        try {
            // DB 操作
          /*  MessageHeaders headers = message.getHeaders();
            String transactionId  =  (String)headers.get(RocketMQHeaders.TRANSACTION_ID);
            String msg = new String((byte[]) message.getPayload(), StandardCharsets.UTF_8);*/
            //int i  =  1/0;

            // 执行DB
            // 执行本地事务
         /*   log.info("执行本地事务提交COMMIT，message:{},arg:{}",message,o);
            result = RocketMQLocalTransactionState.COMMIT;*/
            // 转order对象
            // 执行本地业务逻辑, 如果本地事务执行成功, 则通知Broker可以提交消息让Consumer进行消费
            orderService.add(null);
            localTransMap.put(message.hashCode(),true);
           // result = RocketMQLocalTransactionState.COMMIT;
        } catch (Exception ex) {
            /*log.info("执行异常，本地事务回滚ROLLBACK。message：{}",message);
            result = RocketMQLocalTransactionState.ROLLBACK;*/
            localTransMap.put(message.hashCode(),false);
           // result = RocketMQLocalTransactionState.ROLLBACK;
        }
        return result;
    }

    //checkLocalTransaction 方法用于检查本地事务状态
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        // UNKNOW：未知，这个状态有点意思，如果返回这个状态，这个消息既不提交，也不回滚，还是保持prepared状态，而最终决定这个消息命运的，是checkLocalTransaction这个方法。
        // 回调默认1分钟一次。默认5次UNKNOW，这个消息将被删除。 （brokder.conf中配置）
        // 正常情况下不会调用到
        // 相当于是一个补偿机制
        log.info("【执行检查任务】+message.hashCode:{}",message.hashCode());
        boolean bl = localTransMap.get(message.hashCode());
        // 判断提交或者回滚
        return bl ? RocketMQLocalTransactionState.COMMIT:RocketMQLocalTransactionState.ROLLBACK;
    }
}
