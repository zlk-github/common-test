package com.zlk.producer.service.impl;

import com.zlk.core.model.po.Order;
import com.zlk.producer.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 *
 * @author likuan.zhou
 * @date 2021/11/23/023 19:30
 */
@Service
public class OrderServiceImpl implements OrderService {
    // hashMap线程不安全 速度快(HashMap 适用于单线程操作数据)。多线操作HashMap，我们通过加锁或者加入同步控制依然能正常应用HashMap，只是需要加上同步操作的代价。
    // concurrentMap线程安全 速度慢 分段锁 不是整体锁(concurrentMap适用于多个线程同时要操作一个map的数据). 但是不能保证线程同步顺序。
    private static final ConcurrentHashMap<Long,Order> MAP = new ConcurrentHashMap<>();
    @Override
    public void add(Order order) throws Exception {
        // 模拟入库
        MAP.put(order.getId(),order);
        // 模拟入库异常
        if(order.getId()%2 == 0) {
            throw new Exception();
        }
    }

    @Override
    public void update(Order order) {
        // 模拟更新
        MAP.put(order.getId(),order);
    }

    @Override
    public Order getById(Long id) {
        // 模拟读取
        return MAP.get(id);
    }


}
