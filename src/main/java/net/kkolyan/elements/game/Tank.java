package net.kkolyan.elements.game;

import net.kkolyan.elements.engine.core.ControllerContext;

/**
 * @author nplekhanov
 */
public class Tank extends CombatUnit {

    @Override
    public void afterTick(ControllerContext context) {
        super.afterTick(context);

        if (getFrameRate() == 0) {
            double a = getAngularVelocity().getValue();
            setFrameRate(a / ANGULAR_TRACTION_MULTIPLIER);
        }
    }

    @Override
    public void turn(double timeDirection) {
        getAngularVelocity().add(-timeDirection * getTraction() * ANGULAR_TRACTION_MULTIPLIER / getMass());
//        rotate(-getTurnSpeed() * timeDirection);
        setTracksMoving(true);
    }

    @Override
    public double getTimeToStop() {
        return getAngularVelocity().abs() / (getTraction() * ANGULAR_TRACTION_MULTIPLIER / getMass());
    }
}
