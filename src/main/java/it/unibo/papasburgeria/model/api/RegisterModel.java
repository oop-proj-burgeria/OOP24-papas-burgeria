package it.unibo.papasburgeria.model.api;

import it.unibo.papasburgeria.model.IngredientEnum;
import it.unibo.papasburgeria.model.LineEnum;

import java.util.List;

/**
 * Models how the lines work.
 */
public interface RegisterModel {

    /**
     * Adds a customer to the register line.
     *
     * @param customer Customer added to end of register line.
     * @param line     The line to which the customer is added.
     */
    void addCustomerToLine(CustomerModel customer, LineEnum line);

    /**
     * Removes a customer from the selected line.
     *
     * @param customer Customer added to end of register line.
     * @param line     the line the customer is removed from.
     */
    void removeCustomerFromLine(CustomerModel customer, LineEnum line);

    /**
     * Get the selected line line.
     *
     * @param line the selected line.
     * @return the returned line.
     */
    List<CustomerModel> getLine(LineEnum line);

    /**
     * Empties the line.
     *
     * @param line the selected line.
     */
    void clearLine(LineEnum line);

    /**
     * Starts a new customer thread and kills the previous one.
     *
     * @param delay                defines the rate at which customer arrives
     * @param customerAmount       defines how many customers will be generated by the thread
     * @param availableIngredients defines the possible ingredients in the orders
     */
    void startCustomerThread(int delay, int customerAmount, List<IngredientEnum> availableIngredients);

    /**
     * Stops the customers from arriving.
     */
    void killCustomerThread();

    /**
     * Get the customer thread status.
     *
     * @return true if the customer thread is alive.
     */
    boolean isCustomerThreadStatus();
}
