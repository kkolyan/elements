package net.kkolyan.elements.game;

import net.kkolyan.elements.engine.core.ControllerContext;
import net.kkolyan.elements.engine.core.templates.Vector;
import net.kkolyan.elements.tactics.SurfaceType;

/**
 * @author nplekhanov
 */
public class Car extends CombatUnit {

    private long maxSteeringTime;

    private double steeringTime;

    private boolean turnedLastTick;

    private double vehicleTurningRadius;

    private double maxRearVelocity = Double.POSITIVE_INFINITY;

    public double getMaxRearVelocity() {
        return maxRearVelocity;
    }

    public void setMaxRearVelocity(double maxRearVelocity) {
        this.maxRearVelocity = maxRearVelocity;
    }

    public double getVehicleTurningRadius() {
        return vehicleTurningRadius;
    }

    public void setVehicleTurningRadius(double vehicleTurningRadius) {
        this.vehicleTurningRadius = vehicleTurningRadius;
    }

    public long getMaxSteeringTime() {
        return maxSteeringTime;
    }

    public void setMaxSteeringTime(long maxSteeringTime) {
        this.maxSteeringTime = maxSteeringTime;
    }

    @Override
    public void beforeTick(ControllerContext context) {
        super.beforeTick(context);

        turnedLastTick = false;
    }

    @Override
    public void afterTick(ControllerContext context) {
        super.afterTick(context);

        if (!turnedLastTick) {
            steeringTime = 0;
        }
    }

    @Override
    protected void applySelfSourcedImpulse(Vector impulse) {
        if (getSurfaceType() == SurfaceType.GRASS) {
            impulse.multiply(0.25);
        }
        Vector direction = Vector.fromAngle(getDirection(), 1.0);

        if (impulse.dotProduct(direction) < 0) {
            Vector directionalVelocity = direction.getProjectionOf(getVelocity());
            if (directionalVelocity.dotProduct(direction) < 0) {
                double possibleDirectionalImpulse = maxRearVelocity - directionalVelocity.magnitude();
                if (possibleDirectionalImpulse < 0) {
                    impulse.multiply(0);
                } else {
                    Vector intention = direction.getProjectionOf(impulse);
                    impulse.multiply(Math.min(possibleDirectionalImpulse, intention.magnitude()) / impulse.magnitude());
                }
            }
        }
        super.applySelfSourcedImpulse(impulse);
    }

    @Override
    public void turn(double timeDirection) {
        turnedLastTick = true;

        steeringTime += Math.abs(timeDirection) * 1000.0;
        if (steeringTime > maxSteeringTime) {
            steeringTime = maxSteeringTime;
        }
        timeDirection = timeDirection * steeringTime / maxSteeringTime;

        double rotation = -timeDirection * getVelocity().magnitude() * 2500 / vehicleTurningRadius;
        if (getVelocity().dotProduct(Vector.fromAngle(getDirection(), 1)) < 0) {
            rotation = -rotation;
        }
        rotate(rotation);
//        rotate(-getTurnSpeed() * timeDirection);
        setTracksMoving(true);
    }
}
