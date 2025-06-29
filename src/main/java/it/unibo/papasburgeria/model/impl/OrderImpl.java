package it.unibo.papasburgeria.model.impl;

import it.unibo.papasburgeria.model.IngredientEnum;
import it.unibo.papasburgeria.model.api.Hamburger;
import it.unibo.papasburgeria.model.api.Order;

import java.util.List;

/**
 * Implementation of Order.
 *
 * <p>
 * See {@link Order} for interface details.
 */
public class OrderImpl implements Order {
    private final int orderNumber;
    private final Hamburger hamburger;

    /**
     * Costructor that generates an order with a given Hamburger.
     *
     * @param hamburger   hamburger added to order.
     * @param orderNumber the order number.
     */
    public OrderImpl(final Hamburger hamburger, final int orderNumber) {
        this.hamburger = hamburger.copyOf();
        this.orderNumber = orderNumber;
    }

    /**
     * Costructor that generates an order with a random Hamburger.
     *
     * @param availableIngredients possible ingredients used to generate a random hamburger.
     * @param orderNumber          the order number.
     */
    public OrderImpl(final List<IngredientEnum> availableIngredients, final int orderNumber) {
        this.orderNumber = orderNumber;
        hamburger = HamburgerImpl.generateRandomHamburger(availableIngredients);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Hamburger getHamburger() {
        return hamburger.copyOf();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOrderNumber() {
        return orderNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Order copyOf() {
        return new OrderImpl(hamburger.copyOf(), orderNumber);
    }

    /**
     * @return order and type.
     */
    @Override
    public String toString() {
        return "[Order: [ " + hamburger.toString() + "] ]";
    }
}
