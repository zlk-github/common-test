package com.zlk.producer.controller;


import com.alibaba.fastjson.JSON;
import com.zlk.core.model.po.Order;
import com.zlk.producer.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生产者者事务监听器(事务消息1)  每个事务相当于一个业务处理方法
 * @author likuan.zhou
 * @date 2021/11/1/001 8:33
 */
@Slf4j
@Component
// 默认监控rocketMQTemplate的sendMessageInTransaction
// @RocketMQTransactionListener
// 自定义监听
@RocketMQTransactionListener(rocketMQTemplateBeanName = "extRocketMQTemplate")
public class ProducerTransactionListener implements RocketMQLocalTransactionListener{
    private final AtomicInteger transactionIndex = new AtomicInteger(0);
    // 该记录
    private final ConcurrentHashMap<Long, RocketMQLocalTransactionState> localTransMap=new ConcurrentHashMap<>();
    @Autowired
    private OrderService orderService;

    //executeLocalTransaction 方法来执行本地事务
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        // 会出现5次发送，待处理
        // 默认5次UNKNOW
       /* 事务消息共有三种状态，提交状态、回滚状态、中间状态：
        TransactionStatus.CommitTransaction: 提交事务，它允许消费者消费此消息。
        TransactionStatus.RollbackTransaction: 回滚事务，它代表该消息将被删除，不允许被消费。
        TransactionStatus.Unknown: 中间状态，它代表需要检查消息队列来确定状态。*/
        //String msg = new String((byte[]) message.getPayload(), StandardCharsets.UTF_8);*/

        // 返回UNKNOWN可以测试checkLocalTransaction方法，执行异常可以测试ROLLBACK，localTransMap记录当前状态
        Long id =Long.parseLong((String)message.getHeaders().get("key"));
        RocketMQLocalTransactionState  result;
        // 中间状态，它代表需要检查消息队列来确定状态。
        result = RocketMQLocalTransactionState.UNKNOWN;
        localTransMap.put(id,RocketMQLocalTransactionState.UNKNOWN);
        String msg = new String((byte[]) message.getPayload(), StandardCharsets.UTF_8);
        log.info("本地事务中间状态UNKNOWN。id:{},message:{},arg:{}",id,message,msg);
        try {
            // 执行DB
            // 执行本地业务逻辑, 如果本地事务执行成功, 则通知Broker可以提交消息让Consumer进行消费
            Order order  = JSON.parseObject(msg,Order.class);
            orderService.add(order);

            log.info("执行本地事务提交COMMIT。id:{},message:{},arg:{}",id,message,o);
            result = RocketMQLocalTransactionState.COMMIT;
            localTransMap.put(id,RocketMQLocalTransactionState.COMMIT);
        } catch (Exception ex) {
            // ROLLBACK为失败，需要使用定时任务或者手动补偿。不会走checkLocalTransaction。因为本身入库是失败的。
             log.info("执行异常，本地事务回滚ROLLBACK。id:{},message：{}",id,message);
             result = RocketMQLocalTransactionState.ROLLBACK;
             localTransMap.put(id,RocketMQLocalTransactionState.ROLLBACK);
        }
        return result;
    }

    //checkLocalTransaction 方法用于检查本地事务状态(UNKNOW状态)
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        // UNKNOW：未知，这个状态有点意思，如果返回这个状态，这个消息既不提交，也不回滚，还是保持prepared状态，而最终决定这个消息命运的，是checkLocalTransaction这个方法。
        // 回调默认1分钟一次。默认5次UNKNOW，这个消息将被删除。 （brokder.conf中配置）
        // 正常情况下不会调用到
        // 相当于是一个事务补偿机制
        Long id =Long.parseLong((String)message.getHeaders().get("key"));
        RocketMQLocalTransactionState rocketMQLocalTransactionState = localTransMap.get(id);
        log.info("【执行检查任务】+id:{},transactionState:{}",id,rocketMQLocalTransactionState);
        // 库里面已入成功，提交commit到mq。否则将mq事务回滚
        Order order = orderService.getById(id);
        // 判断提交或者回滚
        if (Objects.isNull(order)) {
            log.info("【执行检查任务结果为ROLLBACK】+order:{},transactionState:{}",order,RocketMQLocalTransactionState.ROLLBACK);
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        log.info("【执行检查任务结果COMMIT】+order:{},transactionState:{}",order,RocketMQLocalTransactionState.ROLLBACK);
        return RocketMQLocalTransactionState.COMMIT;
    }
}
