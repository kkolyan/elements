package net.kkolyan.elements.engine.core.templates;

import net.kkolyan.elements.engine.core.Located;

/**
 * @author nplekhanov
 */
public abstract class OrientedObject2d extends Object2d {
    private double direction;

    public void rotate(double degrees) {
        direction += degrees;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public void set(OrientedObject2d o) {
        set((Located)o);
        setDirection(o.getDirection());
    }
}
