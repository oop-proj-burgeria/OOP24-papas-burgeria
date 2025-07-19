package it.unibo.papasburgeria.controller.impl;

import it.unibo.papasburgeria.model.IngredientEnum;
import it.unibo.papasburgeria.model.LineEnum;
import it.unibo.papasburgeria.model.api.CustomerModel;
import it.unibo.papasburgeria.model.api.GameModel;
import it.unibo.papasburgeria.model.api.HamburgerModel;
import it.unibo.papasburgeria.model.api.IngredientModel;
import it.unibo.papasburgeria.model.api.OrderModel;
import it.unibo.papasburgeria.model.api.RegisterModel;
import it.unibo.papasburgeria.model.impl.CustomerModelImpl;
import it.unibo.papasburgeria.model.impl.GameModelImpl;
import it.unibo.papasburgeria.model.impl.HamburgerModelImpl;
import it.unibo.papasburgeria.model.impl.IngredientModelImpl;
import it.unibo.papasburgeria.model.impl.PantryModelImpl;
import it.unibo.papasburgeria.model.impl.RegisterModelImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/**
 * Test class for {@link OrderSelectionControllerImpl}.
 */
class OrderSelectionControllerImplTest {
    private GameModel gameModel;
    private RegisterModel registerModel;
    private OrderSelectionControllerImpl controller;

    /**
     * Called before each test.
     */
    @BeforeEach
    void setUp() {
        gameModel = new GameModelImpl();
        registerModel = new RegisterModelImpl();
        final PantryModelImpl pantryModel = new PantryModelImpl();

        final CustomerModel customer = new CustomerModelImpl(new ArrayList<>(pantryModel.getUnlockedIngredients()), 1);
        registerModel.addCustomerToLine(customer, LineEnum.WAIT_LINE);

        controller = new OrderSelectionControllerImpl(gameModel, registerModel);
    }

    /**
     * Tests {@link OrderSelectionControllerImpl#getOrders()}.
     */
    @Test
    void testGetOrders() {
        final List<OrderModel> orders = controller.getOrders();
        assertEquals(1, orders.size());
        assertEquals(1, orders.getFirst().getOrderNumber());
    }

    /**
     * Tests {@link OrderSelectionControllerImpl#getHamburger()}.
     */
    @Test
    void testGetHamburger() {
        final HamburgerModel original = gameModel.getHamburgerOnAssembly();
        original.addIngredient(new IngredientModelImpl(IngredientEnum.BOTTOM_BUN));
        gameModel.setHamburgerOnAssembly(original);

        final HamburgerModel copy = controller.getHamburger();
        assertEquals(original.getIngredients(), copy.getIngredients());
        assertNotSame(original, copy);
    }

    /**
     * Tests {@link OrderSelectionControllerImpl#setSelectedOrder(OrderModel)}.
     */
    @Test
    void testSetSelectedOrder() {
        final OrderModel order = registerModel.getLine(LineEnum.WAIT_LINE).getFirst().getOrder();
        controller.setSelectedOrder(order);
        assertEquals(order.getOrderNumber(), gameModel.getSelectedOrder().getOrderNumber());
        assertEquals(order.getHamburger().getIngredients(), gameModel.getSelectedOrder().getHamburger().getIngredients());
    }

    /**
     * Tests {@link OrderSelectionControllerImpl#removeTopBun()}.
     */
    @Test
    void testRemoveTopBun() {
        final HamburgerModel hamburger = new HamburgerModelImpl();
        hamburger.addIngredient(new IngredientModelImpl(IngredientEnum.BOTTOM_BUN));
        hamburger.addIngredient(new IngredientModelImpl(IngredientEnum.TOP_BUN));
        gameModel.setHamburgerOnAssembly(hamburger);

        controller.removeTopBun();

        final List<IngredientModel> ingredients = gameModel.getHamburgerOnAssembly().getIngredients();
        assertEquals(1, ingredients.size());
        assertEquals(IngredientEnum.BOTTOM_BUN, ingredients.getFirst().getIngredientType());
    }
}
