package com.yummynoodlebar.config;

import com.yummynoodlebar.config.CoreConfig;
import com.yummynoodlebar.core.domain.Order;
import com.yummynoodlebar.core.domain.OrderStatus;
import com.yummynoodlebar.core.domain.fixtures.OrdersFixtures;
import com.yummynoodlebar.core.events.orders.*;
import com.yummynoodlebar.core.repository.OrdersRepository;
import com.yummynoodlebar.core.services.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import org.junit.Before;
import static junit.framework.TestCase.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfig.class, JPAConfiguration.class, MVCConfig.class})
@WebAppConfiguration
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class CoreDomainIntegrationTest {

    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Autowired
    OrderService orderService;

    //Should map to PersistToCoreOrdersRepository.class
    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    com.yummynoodlebar.persistence.repository.OrdersRepository persistRepo;

    public void testSameCoreOrder(Order order1, Order order2) {
        assertEquals(order1.getDateTimeOfSubmission(), order2.getDateTimeOfSubmission());
        assertEquals(order1.getOrderItems().size(), order2.getOrderItems().size());
        //todo Deeper inspection of items.
        assertEquals(order1.getKey(), order2.getKey());

    }

    public void testSamePersistantOrder(com.yummynoodlebar.persistence.domain.Order order1, com.yummynoodlebar.persistence.domain.Order order2) {
        assertEquals(order1.getDateTimeOfSubmission(), order2.getDateTimeOfSubmission());
        assertEquals(order1.getOrderItems().size(), order2.getOrderItems().size());
        //todo Deeper inspection of items.
        assertEquals(order1.getId(), order2.getId());
    }

    public com.yummynoodlebar.persistence.domain.Order fixturestandardPersistentOrder() {

        Order order = OrdersFixtures.standardOrder();
        return com.yummynoodlebar.persistence.domain.Order.fromCoreOrder(order);
    }

    //TODO create test to check core objects successfully converted into persitent.
    @Test
    public void testCorrectConvertPersistentToCoreOrder() {
        com.yummynoodlebar.persistence.domain.Order persistantOrder = fixturestandardPersistentOrder();

       Order convertedToCoreOrder = persistantOrder.toCoreOrder();
        assertEquals(persistantOrder.getDateTimeOfSubmission(), convertedToCoreOrder.getDateTimeOfSubmission());
        assertEquals(persistantOrder.getOrderItems().size(), convertedToCoreOrder.getOrderItems().size());
        //todo Deeper inspection of items.
        assertEquals(persistantOrder.getId(), convertedToCoreOrder.getKey().toString());
    }

    @Test
    public void testCorrectConvertCoreToPersistentOrder() {
        Order coreOrder = OrdersFixtures.standardOrder();
        com.yummynoodlebar.persistence.domain.Order convertedToPersistentOrder = com.yummynoodlebar.persistence.domain.Order.fromOrderDetails(coreOrder.toOrderDetails());
        assertEquals(coreOrder.getDateTimeOfSubmission(), convertedToPersistentOrder.getDateTimeOfSubmission());
        assertEquals(coreOrder.getOrderItems().size(), convertedToPersistentOrder.getOrderItems().size());
        //todo Deeper inspection of items.
        assertEquals(coreOrder.getKey().toString(), convertedToPersistentOrder.getId());
    }


    //TODO create test to check persitent objects successfully converted into core.

    @Test
    public void shouldBeAbleToAddANewOrderToTheDataStoreViaThePersistRepo() {
        System.out.println("Class: " + persistRepo.getClass());

        Order order = OrdersFixtures.standardOrder();

        order.addStatus(OrderStatus.fromStatusDetails(new OrderStatusDetails(order.getKey(), UUID.randomUUID(), new Date(), "Order Created")
        ));

        com.yummynoodlebar.persistence.domain.Order persistantOrder = com.yummynoodlebar.persistence.domain.Order.fromCoreOrder(order);

        persistRepo.save(persistantOrder);

        List<com.yummynoodlebar.persistence.domain.Order> orders =  persistRepo.findAll();

        assertEquals(1, persistRepo.findAll().size());

        com.yummynoodlebar.persistence.domain.Order returnedPersistentOrder = persistRepo.findById(order.getKey().toString());
        assertEquals(order.getDateTimeOfSubmission(), returnedPersistentOrder.getDateTimeOfSubmission());
        testSamePersistantOrder(persistantOrder, returnedPersistentOrder);

        //TODO REMOVE THIS INTO SOME KIND OF CORE AND DOMAIN ORDER INTEGRATION TEST.
        Order convertedBackCoreOrder = returnedPersistentOrder.toCoreOrder();
        testSameCoreOrder(order, convertedBackCoreOrder);

    }

    @Test
    public void shouldBeAbleToAddANewOrderToTheDataStoreViaTheCoreRepo() {
        System.out.println("Class: " + ordersRepository.getClass());

        Order order = OrdersFixtures.standardOrder();

        order.addStatus(OrderStatus.fromStatusDetails(new OrderStatusDetails(order.getKey(), UUID.randomUUID(), new Date(), "Order Created")
        ));

        order = ordersRepository.save(order);

        List<Order> orders = ordersRepository.findAll();

        assertEquals(1, ordersRepository.findAll().size());

        testSameCoreOrder(order, ordersRepository.findById(order.getKey()));
    }


    //TODOCUMENT We have already asserted the correctness of the collaboration.
    //This is to check that the wiring in CoreConfig works.
    //We do this by inference.
    @Test
    public void addANewOrderToTheSystem() {

        CreateOrderEvent ev = new CreateOrderEvent(new OrderDetails());


        orderService.createOrder(ev);

        AllOrdersEvent allOrders = orderService.requestAllOrders(new RequestAllOrdersEvent());

        assertEquals(1, allOrders.getOrdersDetails().size());
    }
}
