package it.unibo.papasburgeria.model.impl;

import it.unibo.papasburgeria.model.api.HamburgerModel;
import it.unibo.papasburgeria.model.api.OrderModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/**
 * Test class for {@link OrderModelImpl}.
 */
class OrderModelImplTest {

    private HamburgerModelImpl baseHamburger;
    private OrderModelImpl order;
    private int orderNumber;

    /**
     * Sets up a known hamburger and order before each test.
     */
    @BeforeEach
    void setUp() {
        baseHamburger = new HamburgerModelImpl();
        orderNumber = 1;
        order = new OrderModelImpl(baseHamburger, orderNumber);
    }

    /**
     * Tests {@link OrderModelImpl#getHamburger()}.
     */
    @Test
    void testGetHamburgerReturnsCopy() {
        final HamburgerModel hamburgerCopy = order.getHamburger();
        assertNotSame(baseHamburger, hamburgerCopy, "getHamburger() should return a new instance.");
        assertEquals(baseHamburger.getIngredients(), hamburgerCopy.getIngredients(), "Copied hamburger should match original.");
    }

    /**
     * Tests {@link OrderModelImpl#getOrderNumber()}.
     */
    @Test
    void testGetOrderNumber() {
        assertEquals(orderNumber, order.getOrderNumber(), "getOrderNumber() should return the correct value.");
    }

    /**
     * Tests {@link OrderModelImpl#copyOf()}.
     */
    @Test
    void testCopyOfReturnsCopy() {
        final OrderModel copiedOrder = order.copyOf();
        assertNotSame(order, copiedOrder, "copyOf() should return a different instance.");
        assertEquals(order.getOrderNumber(), copiedOrder.getOrderNumber(),
                "Copied order should have the same order number.");
        assertEquals(order.getHamburger().getIngredients(),
                copiedOrder.getHamburger().getIngredients(), "Hamburgers should be equal.");
    }
}
