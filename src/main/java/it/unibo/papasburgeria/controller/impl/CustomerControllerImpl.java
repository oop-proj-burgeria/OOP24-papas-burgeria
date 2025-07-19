package it.unibo.papasburgeria.controller.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import it.unibo.papasburgeria.controller.api.CustomerController;
import it.unibo.papasburgeria.model.CustomerDifficultyEnum;
import it.unibo.papasburgeria.model.LineEnum;
import it.unibo.papasburgeria.model.UpgradeEnum;
import it.unibo.papasburgeria.model.api.CustomerModel;
import it.unibo.papasburgeria.model.api.GameModel;
import it.unibo.papasburgeria.model.api.HamburgerModel;
import it.unibo.papasburgeria.model.api.IngredientModel;
import it.unibo.papasburgeria.model.api.PantryModel;
import it.unibo.papasburgeria.model.api.PattyModel;
import it.unibo.papasburgeria.model.api.RegisterModel;
import it.unibo.papasburgeria.model.api.ShopModel;
import it.unibo.papasburgeria.model.impl.HamburgerModelImpl;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Manages the appearance and disappearance of customers.
 */
@Singleton
public class CustomerControllerImpl implements CustomerController {
    /**
     * The maximum payment amount.
     */
    public static final int MAX_PAYMENT = 50;
    /**
     * The default tip percentage.
     */
    public static final double DEFAULT_TIP = 0.05;
    private final GameModel model;
    private final ShopModel shop;
    private final RegisterModel registerModel;
    private final PantryModel pantryModel;

    @Inject
    CustomerControllerImpl(
            final GameModel model,
            final ShopModel shop,
            final RegisterModel registerModel,
            final PantryModel pantryModel) {
        this.model = model;
        this.shop = shop;
        this.registerModel = registerModel;
        this.pantryModel = pantryModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serveCustomer(final CustomerModel customer) {
        registerModel.removeCustomerFromLine(customer, LineEnum.WAIT_LINE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearAllCustomers() {
        registerModel.clearLine(LineEnum.REGISTER_LINE);
        registerModel.clearLine(LineEnum.WAIT_LINE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startClientThread() {
        clearAllCustomers();
        final CustomerDifficultyEnum difficulty;
        switch (model.getCurrentDay()) {
            case 1:
                difficulty = CustomerDifficultyEnum.FIRST;
                break;

            case 2:
                difficulty = CustomerDifficultyEnum.SECOND;
                break;

            case 3:
                difficulty = CustomerDifficultyEnum.THIRD;
                break;

            case 4:
                difficulty = CustomerDifficultyEnum.FORTH;
                break;

            default:
                difficulty = CustomerDifficultyEnum.FIFTH;
                break;
        }

        registerModel.startCustomerThread((int) (difficulty.getSpawnIntervalSeconds()
                        + (difficulty.getSpawnIntervalSeconds() * shop.getUpgradeModifier(UpgradeEnum.SLOW_CUSTOMERS))),
                (int) (difficulty.getCustomerCount()
                        - (difficulty.getCustomerCount() * shop.getUpgradeModifier(UpgradeEnum.LESS_CUSTOMERS))),
                pantryModel.getUnlockedIngredients().stream().toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopClientThread() {
        registerModel.killCustomerThread();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCustomerThreadStatus() {
        return registerModel.isCustomerThreadStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CustomerModel> getRegisterLine() {
        return registerModel.getLine(LineEnum.REGISTER_LINE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CustomerModel> getWaitLine() {
        return registerModel.getLine(LineEnum.WAIT_LINE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void takeOrderFromCustomer(final CustomerModel customer) {
        registerModel.removeCustomerFromLine(customer, LineEnum.REGISTER_LINE);
        registerModel.addCustomerToLine(customer, LineEnum.WAIT_LINE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double calculateSatisfactionPercentage(final HamburgerModel startingHamburger,
                                                  final HamburgerModel madeHamburger) {
        final List<IngredientModel> list1 = startingHamburger.getIngredients();
        final List<IngredientModel> list2 = madeHamburger.getIngredients();

        /*if either of them are empty, return satisfacion % 0 */
        if (list1 == null || list2 == null || list1.isEmpty() || list2.isEmpty()) {
            return 0;
        }

        final int minLength = Math.min(list1.size(), list2.size());
        int matchCount = 0;

        double totalCookDifference = 0;
        int pattyComparisons = 0;
        for (int i = 0; i < minLength; i++) {
            final Object a = list1.get(i);
            final Object b = list2.get(i);

            if (a == null || b == null) {
                continue;
            }

            if (a.equals(b) && a.getClass().equals(b.getClass())) {
                matchCount++;
                if (a instanceof PattyModel && b instanceof PattyModel) {
                    final double diff = Math.abs(((PattyModel) a).getBottomCookLevel() - ((PattyModel) b).getBottomCookLevel())
                            + Math.abs(((PattyModel) a).getTopCookLevel() - ((PattyModel) b).getTopCookLevel());

                    totalCookDifference += diff;
                    pattyComparisons++;
                }
            }
        }
        /* calculates how similarly two patties are cooked */
        final double pattySimilarityPercentage;

        if (pattyComparisons > 0) {
            pattySimilarityPercentage = 1.0 - (totalCookDifference / (pattyComparisons * 2.0));
        } else {
            pattySimilarityPercentage = 1.0;
        }

        /* normalize by the max length to penalize extra/missing elements */
        final int maxLength = Math.max(list1.size(), list2.size());
        double similarityPercentage = (double) matchCount / maxLength
                + shop.getUpgradeModifier(UpgradeEnum.INGREDIENT_TOLERANCE);
        if (similarityPercentage > 1.0) {
            similarityPercentage = 1.0;
        }

        double placementAccuracyTotal = 0.0;
        for (final IngredientModel ingredient : list1) {
            placementAccuracyTotal += ingredient.getPlacementAccuracy();
        }
        /* calculates the placement accuracy (1 - (averageAccuracy)) */
        double placementPercentage = 1.0 - (placementAccuracyTotal / list1.size())
                + shop.getUpgradeModifier(UpgradeEnum.PLACEMENT_TOLERANCE);
        if (placementPercentage > 1.0) {
            placementPercentage = 1.0;
        }

        /* calculates the difficulty percentage (size/maxsize) */
        final double ignoreTwoIngredients = 2.0;
        final double difficultyPercentage = (list1.size() - ignoreTwoIngredients) / HamburgerModelImpl.MAX_INGREDIENTS;

        final double similarityPercentageWeight = 6.0;
        final double placementPercentageWeight = 2.0;
        return (difficultyPercentage + similarityPercentage * similarityPercentageWeight
                + placementPercentage * placementPercentageWeight + pattySimilarityPercentage) / 10;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int calculatePayment(final double percentage) {
        return (int) (MAX_PAYMENT * percentage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int calculateTips(final int payment) {
        if (ThreadLocalRandom.current().nextDouble() <= shop.getUpgradeModifier(UpgradeEnum.CUSTOMER_TIP)) {
            if (shop.isUpgradeUnlocked(UpgradeEnum.CUSTOMER_MORE_TIP)) {
                return (int) (payment * shop.getUpgradeModifier(UpgradeEnum.CUSTOMER_MORE_TIP));
            } else {
                return (int) (payment * DEFAULT_TIP);
            }
        } else {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addBalance(final int payment) {
        model.setBalance(model.getBalance() + payment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getBalance() {
        return model.getBalance();
    }
}
