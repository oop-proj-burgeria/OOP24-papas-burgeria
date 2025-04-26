package it.unibo.papasburgeria.model.api;


import it.unibo.papasburgeria.model.IngredientEnum;

public interface Ingredient {

    /*
     * @return The type of ingredient
     */
    IngredientEnum getIngredientType();

    /*
     * @param a double value representing how accurately the ingredient was placed (in %)
     */
    void setPlacementAccuracy(double accuracy);

    /*
     * @return a double value representing how accurately the ingredient was placed (in %)
     */
    double getPlacementAccuracy();

    /*
     * @return a string with the ingredient and their current status
     */
    String toString();
}