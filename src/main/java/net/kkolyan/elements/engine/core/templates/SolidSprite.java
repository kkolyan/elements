package net.kkolyan.elements.engine.core.templates;

import net.kkolyan.elements.engine.core.physics.Body;

/**
 * @author nplekhanov
 */
public class SolidSprite extends Sprite implements Body {

    private Vector velocity = new Vector();
    private double mass;
    private double boundingRadius;
    private double airFrictionFactor;
    private double constantFrictionFactor;
    private double lateralFrictionFactor;
    private MutableDouble angularVelocity = new MutableDouble();

    @Override
    public MutableDouble getAngularVelocity() {
        return angularVelocity;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    @Override
    public Vector getVelocity() {
        return velocity;
    }

    @Override
    public OrientedObject2d getControllableObject() {
        return this;
    }

    @Override
    public double getMass() {
        return mass;
    }

    @Override
    public double getBoundingRadius() {
        return boundingRadius;
    }

    public void setBoundingRadius(double boundingRadius) {
        this.boundingRadius = boundingRadius;
    }

    @Override
    public double getAirFrictionFactor() {
        return airFrictionFactor;
    }

    public void setAirFrictionFactor(double airFrictionFactor) {
        this.airFrictionFactor = airFrictionFactor;
    }

    @Override
    public double getConstantFrictionFactor() {
        return constantFrictionFactor;
    }

    public void setConstantFrictionFactor(double constantFrictionFactor) {
        this.constantFrictionFactor = constantFrictionFactor;
    }

    @Override
    public double getLateralFrictionFactor() {
        return lateralFrictionFactor;
    }

    public void setLateralFrictionFactor(double lateralFrictionFactor) {
        this.lateralFrictionFactor = lateralFrictionFactor;
    }
}
