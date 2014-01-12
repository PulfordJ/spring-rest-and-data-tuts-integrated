package com.yummynoodlebar.persistence.domain.fixture;

import com.yummynoodlebar.persistence.domain.Order;
import com.yummynoodlebar.persistence.domain.OrderStatus;

import java.math.BigDecimal;
import java.util.*;

public class PersistenceFixture {

  public static Order standardOrder() {
    String key = UUID.randomUUID().toString();

    Order order = new Order();
    order.setDateTimeOfSubmission(new Date());
    order.setId(key);

    Map<String, Integer> items = new HashMap<String, Integer>();

    items.put("yummy1", 15);
    items.put("yummy3", 12);
    items.put("yummy5", 7);

    order.setOrderItems(items);

    return order;
  }

  public static Order yummy16Order() {
    String key = UUID.randomUUID().toString();

    Order order = new Order();
    order.setDateTimeOfSubmission(new Date());
    order.setId(key);

    Map<String, Integer> items = new HashMap<String, Integer>();

    items.put("yummy16", 22);

    order.setOrderItems(items);

    return order;
  }

  public static OrderStatus orderReceived(UUID orderId) {
    return new OrderStatus(orderId, UUID.randomUUID(), new Date(), "Order Received");
  }
  public static OrderStatus startedCooking(UUID orderId) {
    return new OrderStatus(orderId, UUID.randomUUID(), new Date(), "Started Cooking");
  }
  public static OrderStatus finishedCooking(UUID orderId) {
    return new OrderStatus(orderId, UUID.randomUUID(), new Date(), "Finished Cooking");
  }
}
