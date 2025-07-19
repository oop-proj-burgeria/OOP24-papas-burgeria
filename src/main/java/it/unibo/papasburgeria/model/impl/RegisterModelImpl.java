package it.unibo.papasburgeria.model.impl;

import com.google.inject.Singleton;
import it.unibo.papasburgeria.model.IngredientEnum;
import it.unibo.papasburgeria.model.LineEnum;
import it.unibo.papasburgeria.model.api.CustomerModel;
import it.unibo.papasburgeria.model.api.RegisterModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of RegisterModel.
 *
 * <p>
 * See {@link RegisterModel} for interface details.
 */
@Singleton
public class RegisterModelImpl implements RegisterModel {
    private final List<List<CustomerModel>> lineList;
    private CustomerThread customerThread;

    /**
     * Constructs the register model.
     */
    public RegisterModelImpl() {
        this.lineList = new ArrayList<>();
        this.lineList.add(new ArrayList<>());
        this.lineList.add(new ArrayList<>());
        this.customerThread = new CustomerThread(0, -1, null, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startCustomerThread(final int delay, final int customerAmount,
                                    final List<IngredientEnum> availableingredients) {
        killCustomerThread();
        customerThread = new CustomerThread(delay, customerAmount, availableingredients, this);
        customerThread.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void killCustomerThread() {
        if (customerThread.isAlive()) {
            customerThread.interrupt();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addCustomerToLine(final CustomerModel customer, final LineEnum lineEnum) {
        customer.setInRegisterLine(true);
        lineList.get(lineEnum.getValue()).add(customer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCustomerFromLine(final CustomerModel customer, final LineEnum lineEnum) {
        customer.setInRegisterLine(false);
        lineList.get(lineEnum.getValue()).remove(customer);
    }

    /**
     * @return the line
     */
    @Override
    public List<CustomerModel> getLine(final LineEnum lineEnum) {
        return List.copyOf(lineList.get(lineEnum.getValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearLine(final LineEnum lineEnum) {
        lineList.get(lineEnum.getValue()).clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[CustomerManager: [registerLine=" + lineList.get(LineEnum.REGISTER_LINE.getValue())
                + "], [waitLine=" + lineList.get(LineEnum.WAIT_LINE.getValue()) + "] ]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCustomerThreadStatus() {
        return customerThread.isAlive();
    }
}
