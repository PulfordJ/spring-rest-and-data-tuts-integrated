package com.yummynoodlebar.persistence.domain.fixture;

import com.yummynoodlebar.config.JPAConfiguration;
import com.yummynoodlebar.persistence.domain.Order;
import com.yummynoodlebar.core.domain.fixtures.OrdersFixtures;
import com.yummynoodlebar.persistence.repository.OrdersRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JPAConfiguration.class})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class PersistentToCoreOrdersRepositoryAdapterTest {
    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    EntityManager entityManager;


  @Before
  public void setupUnitUnderTest() {
        ordersRepository.deleteAll();
  }

  @Test
  public void addASingleOrderToTheOrders() {

    assertEquals(0, ordersRepository.findAll().size());

    ordersRepository.save(Order.fromCoreOrder(OrdersFixtures.standardOrder()));

    assertEquals(1, ordersRepository.findAll().size());
  }

  @Test
  public void removeASingleOrder() {

    com.yummynoodlebar.core.domain.Order coreOrder = OrdersFixtures.standardOrder();

      ordersRepository.save(Order.fromCoreOrder(coreOrder));

    assertEquals(1, ordersRepository.findAll().size());

    ordersRepository.delete(coreOrder.getKey().toString());
      List<Order> allOrders = ordersRepository.findAll();
      for (Order order : allOrders) {
          System.out.println("Order: "+order);
      }
      System.out.println("Size of repo: " + ordersRepository.findAll().size());
    assertEquals(0, ordersRepository.findAll().size());
  }
}
