package net.kkolyan.elements.modeling.gait;

import net.kkolyan.elements.engine.core.templates.Vector;

/**
 * @author nplekhanov
 */
public class Image {
    private String resource;
    private Vector offset = new Vector();
    private Vector scale = new Vector(1, 1);
    private double rotation;

    public Image(String resource) {
        this.resource = resource;
    }

    public Image rotation(double rotation) {
        this.rotation = rotation;
        return this;
    }

    public Image offset(Vector offset) {
        this.offset = offset;
        return this;
    }

    public Image offset(double x, double y) {
        this.offset = new Vector(x, y);
        return this;
    }

    public Image scale(Vector scale) {
        this.scale = scale;
        return this;
    }

    public Image scale(double scale) {
        this.scale = new Vector(scale, scale);
        return this;
    }

    public String getResource() {
        return resource;
    }

    public Vector getOffset() {
        return offset;
    }

    public Vector getScale() {
        return scale;
    }

    public double getRotation() {
        return rotation;
    }
}
