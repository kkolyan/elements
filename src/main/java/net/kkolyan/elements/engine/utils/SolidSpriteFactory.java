package net.kkolyan.elements.engine.utils;

import net.kkolyan.elements.engine.core.templates.SolidSprite;

/**
 * @author nplekhanov
 */
public class SolidSpriteFactory implements ObjectProvider<SolidSprite> {
    private String imageSetId;
    private double airFrictionFactor;
    private double constantFrictionFactor;
    private double scale;
    private double mass;
    private double boundingRadius;

    @Override
    public SolidSprite getObject() {
        SolidSprite sprite = new SolidSprite();
        sprite.setImageSetId(imageSetId);
        sprite.setAirFrictionFactor(airFrictionFactor);
        sprite.setConstantFrictionFactor(constantFrictionFactor);
        sprite.setScale(scale);
        sprite.setMass(mass);
        sprite.setBoundingRadius(boundingRadius);
        return sprite;
    }

    public void setImageSetId(String imageSetId) {
        this.imageSetId = imageSetId;
    }

    public void setAirFrictionFactor(double airFrictionFactor) {
        this.airFrictionFactor = airFrictionFactor;
    }

    public void setConstantFrictionFactor(double constantFrictionFactor) {
        this.constantFrictionFactor = constantFrictionFactor;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setBoundingRadius(double boundingRadius) {
        this.boundingRadius = boundingRadius;
    }
}
