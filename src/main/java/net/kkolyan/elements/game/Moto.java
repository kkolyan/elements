package net.kkolyan.elements.game;

import net.kkolyan.elements.engine.core.graphics.Drawable;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author nplekhanov
 */
public class Moto extends Car {
    private double maxShotVelocity;
    private String movingImageSetId;

    public double getMaxShotVelocity() {
        return maxShotVelocity;
    }

    public void setMaxShotVelocity(double maxShotVelocity) {
        this.maxShotVelocity = maxShotVelocity;
    }

    public String getMovingImageSetId() {
        return movingImageSetId;
    }

    public void setMovingImageSetId(String movingImageSetId) {
        this.movingImageSetId = movingImageSetId;
    }

    @Override
    public String getImageSetId() {
        if (getVelocity().magnitude() > maxShotVelocity) {
            return movingImageSetId;
        }
        return super.getImageSetId();
    }

    @Override
    public <T> Collection<T> of(Class<T> type) {
        if (getVelocity().magnitude() > maxShotVelocity) {
            if (type == Drawable.class) {
                return Arrays.asList(type.cast(this));
            }
        }
        return super.of(type);
    }

    @Override
    public Object shot() {
        if (getVelocity().magnitude() > maxShotVelocity) {
            return null;
        }
        return super.shot();
    }
}
