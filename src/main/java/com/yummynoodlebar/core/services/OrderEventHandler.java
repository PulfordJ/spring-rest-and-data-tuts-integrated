package com.yummynoodlebar.core.services;

import com.yummynoodlebar.core.domain.Order;
import com.yummynoodlebar.core.domain.OrderStatus;
import com.yummynoodlebar.core.events.orders.*;
import com.yummynoodlebar.core.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class OrderEventHandler implements OrderService {


 @Autowired
 private final OrdersRepository ordersRepository;

  public OrderEventHandler(final OrdersRepository ordersRepository) {
    this.ordersRepository = ordersRepository;
  }

  @Override
  public OrderCreatedEvent createOrder(CreateOrderEvent createOrderEvent) {
    Order order = Order.fromOrderDetails(createOrderEvent.getDetails());

    order.addStatus(OrderStatus.fromStatusDetails(new OrderStatusDetails(order.getKey(), UUID.randomUUID(), new Date(), "Order Created")
    ));

    order = ordersRepository.save(order);

    //TODO DELETE THIS FOR EVENT HANDLER TEST TO PASS.
    //List<Order> orders = ordersRepository.findAll();

    return new OrderCreatedEvent(order.getKey(), order.toOrderDetails());
  }

  @Override
  public AllOrdersEvent requestAllOrders(RequestAllOrdersEvent requestAllCurrentOrdersEvent) {
    List<OrderDetails> generatedDetails = new ArrayList<OrderDetails>();
    List<Order> orders = ordersRepository.findAll();
    for (Order order : orders) {
      generatedDetails.add(order.toOrderDetails());
    }
    return new AllOrdersEvent(generatedDetails);
  }

  @Override
  public OrderDetailsEvent requestOrderDetails(RequestOrderDetailsEvent requestOrderDetailsEvent) {

    Order order = ordersRepository.findById(requestOrderDetailsEvent.getKey());

    if (order == null) {
      return OrderDetailsEvent.notFound(requestOrderDetailsEvent.getKey());
    }

    return new OrderDetailsEvent(
            requestOrderDetailsEvent.getKey(),
            order.toOrderDetails());
  }

  @Override
  public OrderUpdatedEvent setOrderPayment(SetOrderPaymentEvent setOrderPaymentEvent) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public OrderDeletedEvent deleteOrder(DeleteOrderEvent deleteOrderEvent) {

    Order order = ordersRepository.findById(deleteOrderEvent.getKey());

    if (order == null) {
      return OrderDeletedEvent.notFound(deleteOrderEvent.getKey());
    }

    OrderDetails details = order.toOrderDetails();

    //TODOCUMENT This contains some specific domain logic, not exposed to the outside world, and not part of the
    //persistence rules.

    if (!order.canBeDeleted()) {
      return OrderDeletedEvent.deletionForbidden(deleteOrderEvent.getKey(), details);
    }

    ordersRepository.delete(deleteOrderEvent.getKey());
    return new OrderDeletedEvent(deleteOrderEvent.getKey(), details);
  }

  @Override
  public OrderStatusEvent requestOrderStatus(RequestOrderStatusEvent requestOrderDetailsEvent) {
    Order order = ordersRepository.findById(requestOrderDetailsEvent.getKey());

    if (order == null) {
      return OrderStatusEvent.notFound(requestOrderDetailsEvent.getKey());
    }

    return new OrderStatusEvent(requestOrderDetailsEvent.getKey(), order.getStatus().toStatusDetails());
  }
}
