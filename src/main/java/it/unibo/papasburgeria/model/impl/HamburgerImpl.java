package it.unibo.papasburgeria.model.impl;

import it.unibo.papasburgeria.model.IngredientEnum;
import it.unibo.papasburgeria.model.api.Hamburger;
import it.unibo.papasburgeria.model.api.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class HamburgerImpl implements Hamburger {

    private final ArrayList<Ingredient> ingredientList;

    public HamburgerImpl() {
        ingredientList = new ArrayList<Ingredient>();
    }

    public HamburgerImpl(ArrayList<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @Override
    public void addIngredient(Ingredient ingredient) throws Exception {
        if (ingredientList.isEmpty() && ingredient.getIngredientType() != IngredientEnum.BOTTOMBUN) {
            throw (new UnsupportedOperationException("Bottom bun MUST be first hamburger ingredient"));
        }

        if (!ingredientList.isEmpty()) {
            if (ingredientList.get(ingredientList.size() - 1).getIngredientType().equals(IngredientEnum.TOPBUN)) {
                throw (new UnsupportedOperationException("Top bun MUST be last hamburger ingredient"));
            }
        }

        ingredientList.add(ingredient);
    }

    @Override
    public List<Ingredient> getIngredients() {
        return ingredientList;
    }

    public String toString() {
        String sb = "[ ";
        for (Ingredient ingredient : ingredientList) {
            sb = sb + ingredient.toString() + ", ";
        }
        sb = sb + "]";

        return sb;
    }

}
