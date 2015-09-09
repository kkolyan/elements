package net.kkolyan.elements.engine.core.templates;

import net.kkolyan.elements.engine.core.Located;

import java.text.DecimalFormat;

/**
 * @author nplekhanov
 */
public class Vector extends Object2d {

    public Vector() {
    }

    public Vector(Located located) {
        set(located);
    }

    public Vector(double x, double y) {
        setX(x);
        setY(y);
    }

    public Vector copy() {
        return new Vector(getX(), getY());
    }

    public Vector getMultiplied(double x, double y) {
        return new Vector(getX() * x, getY() * y);
    }

    public Vector getMultiplied(double multiplier) {
        Vector v = copy();
        v.multiply(multiplier);
        return v;
    }

    public Vector interpolateTo(Located o, double factor) {
        Vector v = new Vector(o);
        v.transpose(getMultiplied(-1));
        v.multiply(factor);
        v.transpose(this);
        return v;
    }

    public double dotProduct(Located o) {
        return getX() * o.getX() + getY() * o.getY();
    }

    public static Vector fromAngle(double angle, double magnitude) {
        Vector vector = new Vector();
        vector.setX(magnitude * Math.cos(angle / 180 * Math.PI));
        vector.setY(magnitude * Math.sin(angle / 180 * Math.PI));
        return vector;
    }

    @Override
    public String toString() {
        return "Vector{"+formatDimension(getX())+","+formatDimension(getY())+",|"+formatDimension(magnitude())+"|}";
    }

    public static void main(String[] args) {
        System.out.println(new Vector(0, 0).getNormalized());
    }

    public Vector getProjectionOf(Located other) {
        double bp = dotProduct(other);

        Vector a = getMultiplied(bp / (magnitude() * magnitude()));
        return a;
    }

    private String formatDimension(double d) {
        String s = String.format("%.5f", d);
        if (d >= 0) {
            s = "+"+s;
        }
        while (s.length() < 11) {
            s = " " + s;
        }
        return s;
    }

    public Vector getSnapped(double resolution) {
        return new Vector(snap(getX(), resolution), snap(getY(), resolution));
    }

    private double snap(double n, double resolution) {
        return Math.round(n / resolution) * resolution;
    }

    public Vector getTranslated(Located o) {
        return new Vector(getX()+o.getX(), getY()+o.getY());
    }

    public Vector getNormalized() {
        return getMultiplied(1.0/magnitude());
    }

    public double cos(Vector o) {
        return dotProduct(o) / (magnitude() * o.magnitude());
    }
}
