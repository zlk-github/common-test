package com.zlk.producer.controller;

import com.zlk.core.model.constant.RocketMQConstant;
import com.zlk.core.model.vo.UserVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

/**
 * 生产者测试用例
 * @author likuan.zhou
 * @date 2021/11/1/001 8:33
 */
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
// 注 类和方法必须为public才能运行
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProducerControllerTest {
    // 注事务消息需要启动服务配合测试
    @Autowired
    private ProducerController producerController;

    @Test
    public void testSyncSend(){
        String msg = "这是一条同步消息20211202！";
        producerController.syncSend(msg);
    }

    @Test
    public void testAsyncSend(){
        String msg = "这是一条异步消息！";
        producerController.asyncSend(msg);
    }


    @Test
    public void testSendOneway(){
        String msg = "这是一条即时间消息（单向消息）！";
        producerController.sendOneway(msg);
    }

    @Test
    public void testTagSend(){
        String msg = "这是一条带tag的消息。tag:";
        producerController.tagSend(msg+RocketMQConstant.TAG_1, RocketMQConstant.TAG_1);
        producerController.tagSend(msg+RocketMQConstant.TAG_2, RocketMQConstant.TAG_2);
        producerController.tagSend(msg+"111", "111");
    }

    @Test
    public void testSendUser(){
        UserVO userVO = new UserVO();
        userVO.setId(1001L);
        userVO.setName("这是一条自定义消息体消息");
        producerController.sendUser(userVO);
    }

    @Test
    public void testSyncSendOrderly(){
        // 预期结构hashkey（订单号）相同的一组数据（一个订单的创建，付款，推送，完成，发送短信），会按消息发送的先后顺序进行消费。
        producerController.syncSendOrderly("订单1001创建","1001");
        producerController.syncSendOrderly("订单1001付款","1001");
        producerController.syncSendOrderly("订单1001推送","1001");

        //穿插
        producerController.syncSendOrderly("订单1003创建","1003");
        producerController.syncSendOrderly("订单1003付款","1003");

        producerController.syncSendOrderly("订单1001完成","1001");
        producerController.syncSendOrderly("订单1001发送短信","1001");

        producerController.syncSendOrderly("订单1002创建","1002");
        producerController.syncSendOrderly("订单1002付款","1002");
        producerController.syncSendOrderly("订单1002推送","1002");
        producerController.syncSendOrderly("订单1002完成","1002");
        producerController.syncSendOrderly("订单1002发送短信","1002");

        //穿插
        producerController.syncSendOrderly("订单1003推送","1003");
        producerController.syncSendOrderly("订单1003完成","1003");
        producerController.syncSendOrderly("订单1003发送短信","1003");
    }

    @Test
    public void testRetrySend(){
        // 消息重试消息
        producerController.retrySend("订单1001创建","1001");

        producerController.retrySend("订单1001发送短信","1001");

    }

    @Test
    public void testSendDelayed(){
        producerController.sendDelayed("这是一条延时消息！！！！");
    }

    @Test
    public void testListSend(){
        // 批量消息,需要不大于4M（消息列表分割,超过需要切割）。
        ArrayList<String> listItem = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            listItem.add("这是一条延时消息，序号"+i);
        }

        producerController.listSend(listItem);
    }



    @Test
    public void testTransactionSend(){
        producerController.transactionSend("这是一条事务消息！！！！");

    }
}
