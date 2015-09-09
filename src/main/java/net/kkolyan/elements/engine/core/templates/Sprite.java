package net.kkolyan.elements.engine.core.templates;

import net.kkolyan.elements.engine.core.graphics.Drawable;

import java.util.*;

/**
 * @author nplekhanov
 */
public class Sprite extends OrientedObject2d implements Drawable {
    private double depth;
    private double scale = 1;
    private double frameIndex;
    private double frameRate = 1;
    private String imageSetId;
    private Map<Class,Object> attachments = new HashMap<Class, Object>();

    @Override
    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    @Override
    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    @Override
    public String getImageSetId() {
        return imageSetId;
    }

    public void setImageSetId(String imageSetId) {
        this.imageSetId = imageSetId;
    }

    @Override
    public double getFrameIndex() {
        return frameIndex;
    }

    @Override
    public void setFrameIndex(double frameIndex) {
        this.frameIndex = frameIndex;
    }

    @Override
    public double getFrameRate() {
        return frameRate;
    }

    @Override
    public void handleAnimationEnd() {
    }

    public double getCullingOffset() {
        return 64;
    }

    public void setFrameRate(double frameRate) {
        this.frameRate = frameRate;
    }

    public Map<Class, Object> getAttachments() {
        return attachments;
    }

    public void attach(Object o) {
        attachments.put(o.getClass(), o);
    }

    public <T> T get(Class<T> type) {
        Object o = attachments.get(type);
        return type.cast(o);
    }
}
