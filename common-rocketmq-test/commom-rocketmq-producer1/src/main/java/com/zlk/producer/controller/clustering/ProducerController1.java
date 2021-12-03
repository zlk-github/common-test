package com.zlk.producer.controller.clustering;

import com.zlk.core.model.constant.RocketMQConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 生产者--集群模式（1对1）
 *     注：集群消费（Clustering）：相同消费组下的消费者都会平均分摊消息。（1对1）
 * @author likuan.zhou
 * @date 2021/11/1/001 8:33
 */
@RestController
@RequestMapping("producer1")
@Slf4j
@Api(tags = "生产者--集群模式（1对1）")
public class ProducerController1 {
    // 同一台机器的同一条消息，MsgId是一样的。

    @Autowired
    private DefaultMQProducer mqProducer;

    @PostMapping("/send")
    @ApiOperation("发送普通消息-同步消息，需要等待Broker的响应")
    public void send(@RequestParam String msg){
        try {
            Message message = new Message(RocketMQConstant.TOPIC_7, RocketMQConstant.TAG_1, msg.getBytes(StandardCharsets.UTF_8));
            // 1 同步发送。需要等待Broker的响应 --（常规下使用最多）
            // 默认发送超时时间为3秒
            SendResult send = mqProducer.send(message);
            // 仅仅只是为了看输出，线上不要打该日志，否认会导致效率问题与占用大量服务器存储空间。
            log.info("消息发送成功。msgId:{}",send.getMsgId());
            // 如果不再发送消息，关闭Producer实例。
            mqProducer.shutdown();
        }catch (Exception ex) {
            log.error("消息发送失败，MQ主机信息：{}，Top:{},Tag:{},消息:{}",mqProducer.getNamesrvAddr(),RocketMQConstant.TOPIC_7,RocketMQConstant.TAG_1,msg,ex);
        }
    }

    @PostMapping("/sync/send")
    @ApiOperation("发送普通消息-异步消息，不需要等待Broker的响应")
    public void syncSend(@RequestParam String msg){
        try {
            // 2 发送异步消息。不需要等待Broker的响应 --（大批量，但是对响应时间要求严的接口等，可以集合计算器countDownLatch 使用）
            // SendResult send = mqProducer.send(message);
            // 根据消息数量实例化倒计时计算器
            final CountDownLatch2 countDownLatch = new CountDownLatch2(100);
            for (int i = 0; i < 100; i++) {
                final int index = i;
                StringBuffer sbf = new StringBuffer();
                sbf.append(msg);
                sbf.append("-");
                sbf.append(index);
                Message message = new Message(RocketMQConstant.TOPIC_7, RocketMQConstant.TAG_1, sbf.toString().getBytes());
                mqProducer.send(message,new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        countDownLatch.countDown();
                        log.info("消息发送成功。index:{},msgId:{}",index,sendResult.getMsgId());
                    }

                    @Override
                    public void onException(Throwable e) {
                        countDownLatch.countDown();
                        log.error("消息发送失败。index:{},msgId:{}",index,e);
                    }
                });
            }
            // 等待5s(任务跑完，或者未跑完等待5S)
            countDownLatch.await(5, TimeUnit.SECONDS);
            // 如果不再发送消息，关闭Producer实例。
            //mqProducer.shutdown();
        }catch (Exception ex) {
            log.error("消息发送失败，MQ主机信息：{}，Top:{},Tag:{},消息:{}",mqProducer.getNamesrvAddr(),RocketMQConstant.TOPIC_7,RocketMQConstant.TAG_1,msg,ex);
        }
    }

    @PostMapping("/sendOneway")
    @ApiOperation("发送普通消息-单向消息，没有任何返回")
    public void sendOneway(@RequestParam String msg){
        try {
            Message message = new Message(RocketMQConstant.TOPIC_7, RocketMQConstant.TAG_1, msg.getBytes());
            // 3 单向发送消息。发送单向消息，没有任何返回结果 (适合日志场景，不关心发送结果)
            mqProducer.sendOneway(message);
            // 仅仅只是为了看输出，线上不要打该日志，否认会导致效率问题与占用大量服务器存储空间。
            log.info("消息发送");
            // 如果不再发送消息，关闭Producer实例。
            //mqProducer.shutdown();
        }catch (Exception ex) {
            log.error("消息发送失败，MQ主机信息：{}，Top:{},Tag:{},消息:{}",mqProducer.getNamesrvAddr(),RocketMQConstant.TOPIC_7,RocketMQConstant.TAG_1,msg,ex);
        }
    }





}
