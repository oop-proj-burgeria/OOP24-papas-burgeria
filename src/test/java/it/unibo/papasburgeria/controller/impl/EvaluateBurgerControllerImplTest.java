package it.unibo.papasburgeria.controller.impl;

import it.unibo.papasburgeria.model.IngredientEnum;
import it.unibo.papasburgeria.model.api.GameModel;
import it.unibo.papasburgeria.model.api.HamburgerModel;
import it.unibo.papasburgeria.model.api.IngredientModel;
import it.unibo.papasburgeria.model.api.OrderModel;
import it.unibo.papasburgeria.model.impl.GameModelImpl;
import it.unibo.papasburgeria.model.impl.HamburgerModelImpl;
import it.unibo.papasburgeria.model.impl.IngredientModelImpl;
import it.unibo.papasburgeria.model.impl.OrderModelImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link EvaluateBurgerControllerImpl}.
 */
class EvaluateBurgerControllerImplTest {

    private GameModel gameModel;
    private EvaluateBurgerControllerImpl controller;

    @BeforeEach
    public void setUp() {
        gameModel = new GameModelImpl();
        controller = new EvaluateBurgerControllerImpl(gameModel);
    }

    /**
     * Test for {@link EvaluateBurgerControllerImpl#getHamburgerOnAssembly()}.
     */
    @Test
    void testGetHamburgerOnAssembly() {
        final HamburgerModel original = new HamburgerModelImpl(List.of(
                new IngredientModelImpl(IngredientEnum.BOTTOM_BUN),
                new IngredientModelImpl(IngredientEnum.LETTUCE),
                new IngredientModelImpl(IngredientEnum.TOP_BUN)));

        gameModel.setHamburgerOnAssembly(original);

        final HamburgerModel copy = controller.getHamburgerOnAssembly();

        assertNotNull(copy);
        assertEquals(original.getIngredients(), ((HamburgerModelImpl) copy).getIngredients());
        assertNotSame(original, copy, "Returned burger should be a copy");
    }

    /**
     * Test for {@link EvaluateBurgerControllerImpl#getSelectedOrder()}.
     */
    @Test
    void testGetSelectedOrder() {
        final HamburgerModel original = new HamburgerModelImpl(List.of(
                new IngredientModelImpl(IngredientEnum.BOTTOM_BUN),
                new IngredientModelImpl(IngredientEnum.LETTUCE),
                new IngredientModelImpl(IngredientEnum.TOP_BUN)));

        gameModel.setSelectedOrder(new OrderModelImpl(original, 0));

        final OrderModel copy = controller.getSelectedOrder();

        assertNotNull(copy);
        assertEquals(original.getIngredients(),
                copy.getHamburger().getIngredients());
        assertNotSame(original, copy, "Returned order should be a copy");
    }

    /**
     * Test for {@link EvaluateBurgerControllerImpl#getSelectedOrder()} when null.
     */
    @Test
    void testGetSelectedOrderWhenNull() {
        gameModel.setSelectedOrder(null);
        assertNull(controller.getSelectedOrder());
    }

    /**
     * Test for {@link EvaluateBurgerControllerImpl#emptyHamburgerOnAssembly()}.
     */
    @Test
    void testEmptyHamburgerOnAssembly() {
        final List<IngredientModel> ingredients = List.of(
                new IngredientModelImpl(IngredientEnum.BOTTOM_BUN),
                new IngredientModelImpl(IngredientEnum.LETTUCE),
                new IngredientModelImpl(IngredientEnum.TOP_BUN));
        final HamburgerModel original = new HamburgerModelImpl(ingredients);

        gameModel.setHamburgerOnAssembly(original);

        controller.emptyHamburgerOnAssembly();

        final HamburgerModelImpl result = (HamburgerModelImpl) gameModel.getHamburgerOnAssembly();
        assertNotNull(result);
        assertTrue(result.getIngredients().isEmpty());
    }
}
