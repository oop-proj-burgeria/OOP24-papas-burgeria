package it.unibo.papasburgeria;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import it.unibo.papasburgeria.di.MainModule;
import it.unibo.papasburgeria.model.IngredientEnum;
import it.unibo.papasburgeria.model.api.Hamburger;
import it.unibo.papasburgeria.model.impl.HamburgerImpl;
import it.unibo.papasburgeria.model.impl.IngredientImpl;
import it.unibo.papasburgeria.model.impl.MeatImpl;

/**
 * Main class.
 */
public class Main {

    /**
     * Main method.
     * @param args arguments
     */
    public static void main(final String[] args) {
        // Using production stage to construct services before the game logic starts
        final Injector injector = Guice.createInjector(Stage.PRODUCTION, new MainModule());

        Hamburger burger1 = new HamburgerImpl();
        try {
            burger1.addIngredient(new IngredientImpl(IngredientEnum.BOTTOMBUN));
            burger1.addIngredient(new MeatImpl());
            burger1.addIngredient(new IngredientImpl(IngredientEnum.BBQ));
            burger1.addIngredient(new IngredientImpl(IngredientEnum.KETCHUP));
            burger1.addIngredient(new IngredientImpl(IngredientEnum.MAYO));
            burger1.addIngredient(new IngredientImpl(IngredientEnum.TOMATO));
            burger1.addIngredient(new IngredientImpl(IngredientEnum.LETTUCE));
            burger1.addIngredient(new IngredientImpl(IngredientEnum.TOPBUN));
            System.out.println(burger1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
