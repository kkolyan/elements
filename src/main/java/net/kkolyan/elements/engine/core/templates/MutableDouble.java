package net.kkolyan.elements.engine.core.templates;

/**
 * @author nplekhanov
 */
public class MutableDouble {
    private double value;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public MutableDouble add(double value) {
        this.value += value;
        return this;
    }

    public double abs() {
        return Math.abs(value);
    }
}
