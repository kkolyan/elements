package net.kkolyan.elements.engine.core.physics;

import net.kkolyan.elements.engine.core.templates.Vector;

/**
 * @author nplekhanov
 */
public class Border {
    private Vector begin;
    private Vector end;

    public Border() {
    }

    public Border(Vector begin, Vector end) {
        this.begin = begin;
        this.end = end;
    }

    public void setBegin(Vector begin) {
        this.begin = begin;
    }

    public void setEnd(Vector end) {
        this.end = end;
    }

    public Vector getBegin() {
        return begin;
    }

    public Vector getEnd() {
        return end;
    }
}
