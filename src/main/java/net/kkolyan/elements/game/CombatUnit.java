package net.kkolyan.elements.game;

import net.kkolyan.elements.engine.core.ControllerContext;
import net.kkolyan.elements.engine.core.Located;
import net.kkolyan.elements.engine.core.Shot;
import net.kkolyan.elements.engine.core.graphics.Drawable;
import net.kkolyan.elements.engine.core.templates.SolidSprite;
import net.kkolyan.elements.engine.core.templates.Vector;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author nplekhanov
 */
public class CombatUnit extends SolidSprite implements TickListener,UniObject {
    private Weapon weapon;
    private double traction;
    private double turnSpeed;

    private long readyToShotTimeMillis;

    private double cannonPower;
    private long reloadTime;

    private double movingFrameRate;

    private double towerRotationLimit = 360;

    private double skiddingThreshold = 100;

    public void setSkiddingThreshold(double skiddingThreshold) {
        this.skiddingThreshold = skiddingThreshold;
    }

    private double towerRotationRate = Double.POSITIVE_INFINITY;

    public double getTowerRotationRate() {
        return towerRotationRate;
    }

    public void setTowerRotationRate(double towerRotationRate) {
        this.towerRotationRate = towerRotationRate;
    }

    public double getTowerRotationLimit() {
        return towerRotationLimit;
    }

    public void setTowerRotationLimit(double towerRotationLimit) {
        this.towerRotationLimit = towerRotationLimit;
    }

    public double getMovingFrameRate() {
        return movingFrameRate;
    }

    public void setMovingFrameRate(double movingFrameRate) {
        this.movingFrameRate = movingFrameRate;
    }

    public double getCannonPower() {
        return cannonPower;
    }

    public void setReloadTime(long reloadTime) {
        this.reloadTime = reloadTime;
    }

    public void setCannonPower(double cannonPower) {
        this.cannonPower = cannonPower;
    }

    public double getTraction() {
        return traction;
    }

    public void setTraction(double traction) {
        this.traction = traction;
    }

    public double getTurnSpeed() {
        return turnSpeed;
    }

    public void setTurnSpeed(double turnSpeed) {
        this.turnSpeed = turnSpeed;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void setTracksMoving(boolean tracksMoving) {
        if (tracksMoving) {
            setFrameRate(movingFrameRate);
        } else {
            setFrameRate(0);
        }
    }

    @Override
    public void beforeTick(ControllerContext context) {

        setTracksMoving(false);
    }

    private double limitAngle(double base, double angle, double limit) {
        if (Math.abs(normalizeAngle(angle - base)) > limit) {
            if (normalizeAngle(angle - base) > 0) {
                return base + limit;
            }
            return base - limit;
        }
        return angle;
    }

    @Override
    public void afterTick(ControllerContext context) {
        weapon.setDepth(getDepth() - 3 * getScale());
        weapon.setScale(getScale());
        weapon.set((Located) this);

        Vector dirVector = Vector.fromAngle(getDirection(), 1);
        double m = dirVector.getProjectionOf(getVelocity()).magnitude();
        if (dirVector.dotProduct(getVelocity()) < 0) {
            m = -m;
        }
        if (Math.abs(m) < skiddingThreshold) {
            setFrameRate(m);
        } else {
            setFrameRate(0);
        }
    }

    public void aimTower(double tickLength, Located target) {
        if (target == null) {
            return;
        }

        double desiredTowerDirection;
        double angle = vectorTo(target).angle();
        angle = limitAngle(getDirection(), angle, towerRotationLimit);
        desiredTowerDirection = angle;

        double d;
        double req = normalizeAngle(desiredTowerDirection - weapon.getDirection());
        if (req > 0) {
            d = Math.min(req, towerRotationRate*tickLength);
        } else {
            d = -Math.min(-req, towerRotationRate*tickLength);
        }
        weapon.setDirection(weapon.getDirection()+d);
    }

    public double normalizeAngle(double a) {
        while (a >= 180) {
            a -= 360;
        }
        while (a < -180) {
            a += 360;
        }
        return a;
    }

    public Object shot() {
        if (System.currentTimeMillis() < readyToShotTimeMillis) {
            return null;
        }
        readyToShotTimeMillis = System.currentTimeMillis() + reloadTime;
        weapon.shot();
        return new Shot(this, getTowerDirection(), getCannonPower());
    }

    public void move(double timeDirection) {

        Vector impulse = Vector.fromAngle(getDirection(), getTraction() * timeDirection / getMass());
        applySelfSourcedImpulse(impulse);
        setTracksMoving(true);
    }
    
    protected void applySelfSourcedImpulse(Vector impulse) {
        getVelocity().transpose(impulse);
    }

    public void turn(double timeDirection) {
        rotate(-getTurnSpeed() * timeDirection);
        setTracksMoving(true);
    }

    public double getTowerDirection() {
        return weapon.getDirection();
    }

    public static final double ANGULAR_TRACTION_MULTIPLIER = 100;

    public double getTimeToStop() {
        return 0;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    @Override
    public boolean is(Class<?> type) {
        return type.isInstance(type);
    }

    @Override
    public <T> T as(Class<T> type) {
        return type.cast(type);
    }

    protected Drawable getWeaponAsDrawable() {
        return getWeapon();
    }

    @Override
    public <T> Collection<T> of(Class<T> type) {
        if (type == Drawable.class) {
            return Arrays.asList(type.cast(this), type.cast(getWeaponAsDrawable()));
        }
        if (is(type)) {
            return Collections.singleton(type.cast(this));
        }
        return Collections.emptyList();
    }
}
