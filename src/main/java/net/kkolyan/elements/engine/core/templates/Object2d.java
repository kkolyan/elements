package net.kkolyan.elements.engine.core.templates;

import net.kkolyan.elements.engine.core.Locatable;
import net.kkolyan.elements.engine.core.Located;

/**
 * @author nplekhanov
 */
public abstract class Object2d implements Located, Locatable {
    private double x;
    private double y;

    @Override
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double distanceTo(Located o) {
        return vectorTo(o).magnitude();
    }

    public double angleTo(Located o) {
        return vectorTo(o).angle();
    }

    public void set(Located o) {
        this.x = o.getX();
        this.y = o.getY();
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double magnitude() {
        return Math.sqrt(getX() * getX() + getY() * getY());
    }

    public double angle() {
        return Math.atan2(getY(), getX()) * 180 / Math.PI;
    }

    public void transpose(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public void transpose(Located o) {
        this.x += o.getX();
        this.y += o.getY();
    }

    public Vector vectorTo(Located o) {
        return new Vector(o.getX() - getX(), o.getY() - getY());
    }

    public void multiply(double factor) {
        this.x *= factor;
        this.y *= factor;
    }
}
