package it.unibo.papasburgeria.model.api;

import java.util.List;

public interface Hamburger {
    /*
     * @param ingredient to add to the hamburger
     */
    void addIngredient(Ingredient ingredient) throws Exception;

    /*
     * @return all the ingredient added to the burger
     */
    List<Ingredient> getIngredients();

    /*
     * @return a string with all ingredients and their status
     */
    String toString();
}
