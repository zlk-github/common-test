package com.zlk.producer.controller.clustering;

import com.zlk.core.model.constant.RocketMQConstant;
import com.zlk.core.model.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * 生产者--集群模式（1对1）
 *     注：集群消费（Clustering）：相同消费组下的消费者都会平均分摊消息。（1对1）
 * @author likuan.zhou
 * @date 2021/11/1/001 8:33
 */
@RestController
@RequestMapping("producer")
@Slf4j
@Api(tags = "生产者--集群模式（1对1）")
public class ProducerController {
    // 同一台机器的同一条消息，MsgId是一样的。
    // 以下的log.info不要在生产环境使用，否则会生成大量日志

    @Autowired
    private DefaultMQProducer mqProducer;

    // 使用下面这个模板即可
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @PostMapping("/sync/send")
    @ApiOperation("发送同步消息，需要等待Broker的响应")
    public void syncSend(@RequestParam String msg){
        try {
            // 1 同步发送。需要等待Broker的响应 --（常规下使用最多）
            // TOP主题与消息（默认发送超时时间为3秒）
            SendResult send = rocketMQTemplate.syncSend(RocketMQConstant.CLUSTERING_TOPIC_1, msg);

            // 超时设置(未测试)
            //SendResult send = rocketMQTemplate.syncSend(RocketMQConstant.CLUSTERING_TOPIC_1, msg,3000L);

            // 仅仅只是为了看输出，线上不要打该日志，否认会导致效率问题与占用大量服务器存储空间。
            log.info("消息发送成功。msgId:{}",send.getMsgId());
        }catch (Exception ex) {
            log.error("消息发送失败，MQ主机信息：{}，Top:{},Tag:{},消息:{}",mqProducer.getNamesrvAddr(),RocketMQConstant.CLUSTERING_TOPIC_1,RocketMQConstant.TAG_1,msg,ex);
        }
    }

    @PostMapping("/async/send")
    @ApiOperation("发送异步消息，不需要等待Broker的响应")
    public void asyncSend(@RequestParam String msg){
        try {
            // 2 发送异步消息。不需要等待Broker的响应 --（大批量，但是对响应时间要求严的接口等，可以集合计算器countDownLatch 使用）
            // 根据消息数量实例化倒计时计算器
            final CountDownLatch2 countDownLatch = new CountDownLatch2(100);
            for (int i = 0; i < 100; i++) {
                final int index = i;
                StringBuffer sbf = new StringBuffer();
                sbf.append(msg);
                sbf.append("-");
                sbf.append(index);
                rocketMQTemplate.asyncSend(RocketMQConstant.CLUSTERING_TOPIC_1,msg,new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        countDownLatch.countDown();
                        log.info("消息发送成功。index:{},msgId:{}",index,sendResult.getMsgId());
                    }
                    @Override
                    public void onException(Throwable e) {
                        countDownLatch.countDown();
                        log.error("消息发送失败。index:{}",index,e);
                    }
                });
            }
            // 等待5s(任务跑完，或者未跑完等待5S)
            countDownLatch.await(5, TimeUnit.SECONDS);
        }catch (Exception ex) {
            log.error("消息发送失败，MQ主机信息：{}，Top:{},Tag:{},消息:{}",mqProducer.getNamesrvAddr(),RocketMQConstant.CLUSTERING_TOPIC_1,RocketMQConstant.TAG_1,msg,ex);
        }
    }

    @PostMapping("/sendOneway")
    @ApiOperation("发送即发即时间消息（单向消息），没有任何返回")
    public void sendOneway(@RequestParam String msg){
        try {
            // 3 单向发送消息。发送单向消息，没有任何返回结果 (适合日志场景，不关心发送结果)
            //rocketMQTemplate.convertAndSend(RocketMQConstant.CLUSTERING_TOPIC_1,msg);

            rocketMQTemplate.sendOneWay(RocketMQConstant.CLUSTERING_TOPIC_1,msg);
            log.info("消息发送");
        }catch (Exception ex) {
            log.error("消息发送失败，MQ主机信息：{}，Top:{},Tag:{},消息:{}",mqProducer.getNamesrvAddr(),RocketMQConstant.CLUSTERING_TOPIC_1,RocketMQConstant.TAG_1,msg,ex);
        }
    }


    @PostMapping("/tag/send")
    @ApiOperation("发送消息（top+tag）")
    public void tagSend(@RequestParam String msg){
        try {
            // 带tag发送
            // top+tag（同步）
            //SendResult send = rocketMQTemplate.syncSend(RocketMQConstant.CLUSTERING_TOPIC_1+":"+RocketMQConstant.TAG_1, msg);

            // top+tag（异步）
            /* rocketMQTemplate.asyncSend(RocketMQConstant.CLUSTERING_TOPIC_1+":"+RocketMQConstant.TAG_1,msg,new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("消息发送成功。msgId:{}",sendResult.getMsgId());
                }
                @Override
                public void onException(Throwable e) {
                    log.error("消息发送失败。",e);
                }
            });*/

            // topic: clustering-topic1，tag: clustering-tag1,clustering-tag2 （单向消息）
            rocketMQTemplate.convertAndSend(RocketMQConstant.CLUSTERING_TOPIC_1+":"+RocketMQConstant.TAG_1,msg);
            rocketMQTemplate.convertAndSend(RocketMQConstant.CLUSTERING_TOPIC_1+":"+RocketMQConstant.TAG_2,msg);
            log.info("消息发送");
        }catch (Exception ex) {
            log.error("消息发送失败，MQ主机信息：{}，Top:{},Tag:{},消息:{}",mqProducer.getNamesrvAddr(),RocketMQConstant.CLUSTERING_TOPIC_1,RocketMQConstant.TAG_1,msg,ex);
        }
    }


    @PostMapping("/send/user")
    @ApiOperation("消息发送（消息体为自定义对象）")
    public void sendUser(@RequestBody UserVO userVO){
        try {
            // 自定义消息体
            SendResult send = rocketMQTemplate.syncSend(RocketMQConstant.CLUSTERING_TOPIC_2, userVO);
            log.info("消息发送,msgId:{}",send.getMsgId());
        }catch (Exception ex) {
            log.error("消息发送失败，MQ主机信息：{}，Top:{},消息:{}",mqProducer.getNamesrvAddr(),RocketMQConstant.CLUSTERING_TOPIC_2,userVO,ex);
        }
    }


   @PostMapping("/send/delayed")
    @ApiOperation("发送延时队列")
    public void sendDelayed(@RequestBody String msg){
        try {
            // 发送延时队列
            // TODO 待测试完善
            SendResult send = rocketMQTemplate.syncSend(RocketMQConstant.CLUSTERING_TOPIC_3, MessageBuilder.withPayload(msg).build(), 3000, 3);
            log.info("消息发送,msgId:{}",send.getMsgId());
        }catch (Exception ex) {
            log.error("消息发送失败，MQ主机信息：{}，Top:{},消息:{}",mqProducer.getNamesrvAddr(),RocketMQConstant.CLUSTERING_TOPIC_3,msg,ex);
        }
    }

    @PostMapping("/sync/send/orderly")
    @ApiOperation("发送顺序消息")
    public void syncSendOrderly(@RequestBody String msg,String hashkey){
        try {
            //消息有序指的是可以按照消息的发送顺序来消费(FIFO)。RocketMQ可以严格的保证消息有序，可以分为分区有序或者全局有序。
            // 在默认的情况下消息发送会采取Round Robin轮询方式把消息发送到不同的queue(分区队列)；而消费消息的时候从多个queue上拉取消息，这种情况发送和消费是不能保证顺序。
            // 1.全局有序：控制发送的顺序消息只依次发送到同一个queue中，消费的时候只从这个queue上依次拉取。
            // 2.多个queue参与，则为分区有序，即相对每个queue，消息都是有序的。-- 常见

            //订单进行分区有序的示例。一个订单的顺序流程是：创建、付款、推送、完成。订单号相同的消息会被先后发送到同一个队列中，消费时，
            // 同一个OrderId获取到的肯定是同一个队列。

            // 发送顺序消息：预期结构hashkey（订单号）相同的一组数据（一个订单的创建，付款，推送，完成，发送短信），会按消息发送的先后顺序进行消费。
            //SendResult send = rocketMQTemplate.syncSendOrderly(RocketMQConstant.CLUSTERING_TOPIC_4, MessageBuilder.withPayload(msg).build(), sort);
            SendResult send = rocketMQTemplate.syncSendOrderly(RocketMQConstant.CLUSTERING_TOPIC_4, msg, hashkey);
            log.info("消息发送,msgId:{}",send.getMsgId());
        }catch (Exception ex) {
            log.error("消息发送失败，MQ主机信息：{}，Top:{},消息:{}",mqProducer.getNamesrvAddr(),RocketMQConstant.CLUSTERING_TOPIC_4,msg,ex);
        }
    }


    @PostMapping("/retry/send")
    @ApiOperation("失败重试消息")
    public void retrySend(@RequestBody String message, String hashkey){
        try {
            SendResult send = rocketMQTemplate.syncSendOrderly(RocketMQConstant.CLUSTERING_TOPIC_5, message, hashkey);
            log.info("消息发送,msgId:{}",send.getMsgId());
        }catch (Exception ex) {
            log.error("消息发送失败，MQ主机信息：{}，Top:{},消息:{}",mqProducer.getNamesrvAddr(),RocketMQConstant.CLUSTERING_TOPIC_5,message,ex);
        }
    }


}
