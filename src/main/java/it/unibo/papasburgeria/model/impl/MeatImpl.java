package it.unibo.papasburgeria.model.impl;

import it.unibo.papasburgeria.model.IngredientEnum;

public class MeatImpl extends IngredientImpl {

    private double cookLevel;

    public MeatImpl() {
        super(IngredientEnum.MEAT);
        cookLevel = 0;
    }

    public double getCookLevel() {
        return cookLevel;
    }

    public void setCookLevel(double cookLevel) {
        this.cookLevel = cookLevel;
    }

    public String toString() {
        return "[ type:" + this.getIngredientType() + ", acc:" + this.getPlacementAccuracy() + ", cook:" + this.getCookLevel() + " ]";
    }
}
