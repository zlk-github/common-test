package com.zlk.producer.service;

import com.zlk.core.model.po.Order;

/**
 * TODO
 *
 * @author likuan.zhou
 * @date 2021/11/23/023 19:28
 */
public interface OrderService {

    /**
     * 插入数据
     * @param order 订单
     */
    void add(Order order);

    /**
     * 更新数据
     * @param order 订单
     */
    void update(Order order);

    /**
     * id 获取数据
     * @param id id
     */
    Order getById(Long id);
}
