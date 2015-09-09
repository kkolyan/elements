package net.kkolyan.elements.game.tmx.model;

import org.simpleframework.xml.Attribute;

/**
 * @author nplekhanov
 */
public class TmxImage {

    @Attribute private String source;
    @Attribute private int width;
    @Attribute private int height;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
