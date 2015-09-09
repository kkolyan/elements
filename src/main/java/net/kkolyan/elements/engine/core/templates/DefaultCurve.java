package net.kkolyan.elements.engine.core.templates;

import net.kkolyan.elements.engine.core.Located;
import net.kkolyan.elements.engine.core.graphics.Curve;

import java.util.List;

/**
 * @author nplekhanov
 */
public class DefaultCurve implements Curve {
    private List<? extends Located> points;
    private int width;

    public DefaultCurve(List<? extends Located> points, int width) {
        this.points = points;
        this.width = width;
    }

    public DefaultCurve(List<? extends Located> points) {
        this.points = points;
    }

    @Override
    public List<? extends Located> getPoints() {
        return points;
    }

    @Override
    public int getWidth() {
        return width;
    }
}
