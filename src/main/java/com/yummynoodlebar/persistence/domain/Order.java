package com.yummynoodlebar.persistence.domain;

import com.yummynoodlebar.core.events.orders.OrderDetails;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Entity(name = "NOODLE_ORDERS")
public class Order {

    @Column(name = "SUBMISSION_DATETIME")
    private Date dateTimeOfSubmission;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = java.lang.Integer.class)
    @JoinTable(name="ORDER_ORDER_ITEMS", joinColumns=@JoinColumn(name="ID"))
    @MapKeyColumn(name="MENU_ID")
    @Column(name="VALUE")
    private Map<String, Integer> orderItems;

    @Transient
    private OrderStatus orderStatus;

    @Id
    @Column(name = "ORDER_ID")
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public void setDateTimeOfSubmission(Date dateTimeOfSubmission) {
        this.dateTimeOfSubmission = dateTimeOfSubmission;
    }

    public OrderStatus getStatus() {
        return orderStatus;
    }

    public void setStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getDateTimeOfSubmission() {
        return dateTimeOfSubmission;
    }

    public String getId() {
        return id;
    }

    public void setOrderItems(Map<String, Integer> orderItems) {
        if (orderItems == null) {
            this.orderItems = Collections.emptyMap();
        } else {
            this.orderItems = Collections.unmodifiableMap(orderItems);
        }
    }

    public Map<String, Integer> getOrderItems() {
        return orderItems;
    }

    public OrderDetails toOrderDetails() {
        OrderDetails details = new OrderDetails();

        details.setKey(UUID.fromString(this.id));
        details.setDateTimeOfSubmission(this.dateTimeOfSubmission);
        details.setOrderItems(this.getOrderItems());

        return details;
    }

    public static Order fromOrderDetails(OrderDetails orderDetails) {
        Order order = new Order();

        order.id = orderDetails.getKey().toString();
        order.dateTimeOfSubmission = orderDetails.getDateTimeOfSubmission();
        order.orderItems = orderDetails.getOrderItems();

        return order;
    }

    public static Order fromCoreOrder(com.yummynoodlebar.core.domain.Order order) {
        return Order.fromOrderDetails(order.toOrderDetails());
    }

    public com.yummynoodlebar.core.domain.Order toCoreOrder() {
        com.yummynoodlebar.core.domain.Order order = new com.yummynoodlebar.core.domain.Order(UUID.fromString(this.getId()), this.getDateTimeOfSubmission());
        order.setOrderItems(this.getOrderItems());


        return order;
    }
}

