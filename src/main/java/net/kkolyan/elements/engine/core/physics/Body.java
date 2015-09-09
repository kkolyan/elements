package net.kkolyan.elements.engine.core.physics;

import net.kkolyan.elements.engine.core.templates.MutableDouble;
import net.kkolyan.elements.engine.core.templates.OrientedObject2d;
import net.kkolyan.elements.engine.core.templates.Vector;

/**
 * @author nplekhanov
 */
public interface Body {

    Vector getVelocity();

    OrientedObject2d getControllableObject();

    double getMass();

    double getBoundingRadius();

    double getAirFrictionFactor();

    double getConstantFrictionFactor();

    double getLateralFrictionFactor();

    MutableDouble getAngularVelocity();
}
