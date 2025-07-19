package it.unibo.papasburgeria.model;

/**
 * Enumerates the lines present in RegisterModel.
 */
public enum LineEnum {
    /**
     * Defines the line where customers wait to give their order to the register.
     */
    REGISTER_LINE(0, "register_line"),
    /**
     * Defines the line where customers wait their orders.
     */
    WAIT_LINE(1, "wait_line");

    /**
     * Line value id.
     */
    private final int value;

    /**
     * Line name id.
     */
    private final String lineName;

    /**
     * Contructs a new LineEnum.
     *
     * @param value    line id.
     * @param lineName line name.
     */
    LineEnum(final int value, final String lineName) {
        this.value = value;
        this.lineName = lineName;
    }

    /**
     * Gets the line value id.
     *
     * @return the value.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Gets the line name.
     *
     * @return line name.
     */
    public String getName() {
        return this.lineName;
    }
}
