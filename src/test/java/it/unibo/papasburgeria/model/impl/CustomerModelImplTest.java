package it.unibo.papasburgeria.model.impl;

import it.unibo.papasburgeria.model.api.CustomerModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for {@link CustomerModelImpl}.
 */
class CustomerModelImplTest {
    private CustomerModel customer;
    private OrderModelImpl order;

    /**
     * Called before each test.
     */
    @BeforeEach
    void setUp() {
        final int orderNumber = 0;
        this.order = new OrderModelImpl(new HamburgerModelImpl(), orderNumber);
        this.customer = new CustomerModelImpl(order);
    }

    /**
     * Tests {@link CustomerModelImpl#getOrder()}.
     */
    @Test
    void testGetOrder() {
        assertEquals(order, customer.getOrder());
    }
}
