package net.kkolyan.elements.tactics;

import net.kkolyan.elements.engine.core.templates.Sprite;

/**
 * @author nplekhanov
 */
public class Surface extends Sprite {
    private SurfaceType type;

    public SurfaceType getType() {
        return type;
    }

    public void setType(SurfaceType type) {
        this.type = type;
    }
}
