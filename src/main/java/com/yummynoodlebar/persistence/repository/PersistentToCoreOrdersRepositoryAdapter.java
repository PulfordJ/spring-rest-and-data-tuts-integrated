package com.yummynoodlebar.persistence.repository;

import com.yummynoodlebar.persistence.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by john on 10/01/14.
 */
public class PersistentToCoreOrdersRepositoryAdapter implements com.yummynoodlebar.core.repository.OrdersRepository {
    @Override
    public com.yummynoodlebar.core.domain.Order save(com.yummynoodlebar.core.domain.Order order) {

        Order persistantOrder = Order.fromCoreOrder(order);
        ordersRepository.save(persistantOrder);

        List<Order> orders = ordersRepository.findAll();
        return order;
    }

    @Override
    public void delete(UUID key) {
        ordersRepository.delete(key.toString());
    }

    @Override
    public com.yummynoodlebar.core.domain.Order findById(UUID key) {
        Order order = ordersRepository.findById(key.toString());
        return ordersRepository.findById(key.toString()).toCoreOrder();
    }

    @Override
    public List<com.yummynoodlebar.core.domain.Order> findAll() {
        List<Order> persistOrders = ordersRepository.findAll();
        List<com.yummynoodlebar.core.domain.Order> coreOrders = new ArrayList<com.yummynoodlebar.core.domain.Order>();
        for (Order order : persistOrders) {
            coreOrders.add(com.yummynoodlebar.core.domain.Order.fromOrderDetails(order.toOrderDetails()));
        }
        return coreOrders;
    }

    @Autowired
    OrdersRepository ordersRepository;
}
