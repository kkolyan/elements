package net.kkolyan.elements.engine.core.templates;

import net.kkolyan.elements.engine.core.Located;
import net.kkolyan.elements.engine.core.graphics.Rectangle;

/**
 * @author nplekhanov
 */
public class SimpleRectangle extends Object2d implements Rectangle {
    private Located size;
    private String frameColor;
    private String fillColor;

    public SimpleRectangle() {
    }

    public SimpleRectangle(Located center, Located size, String frameColor, String fillColor) {
        set(center);
        this.frameColor = frameColor;
        this.fillColor = fillColor;
        this.size = size;
    }

    public SimpleRectangle(Located size, String frameColor, String fillColor) {
        this.frameColor = frameColor;
        this.fillColor = fillColor;
        this.size = size;
    }

    @Override
    public Located getSize() {
        return size;
    }

    @Override
    public String getFillColor() {
        return fillColor;
    }

    @Override
    public String getFrameColor() {
        return frameColor;
    }

    public void setSize(Located size) {
        this.size = size;
    }

    public void setFrameColor(String frameColor) {
        this.frameColor = frameColor;
    }

    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    @Override
    public String toString() {
        return "SimpleRectangle{" +
                "center=" + new Vector(this) +
                ", size=" + size +
                ", frameColor='" + frameColor + '\'' +
                ", fillColor='" + fillColor + '\'' +
                "} " + super.toString();
    }
}
