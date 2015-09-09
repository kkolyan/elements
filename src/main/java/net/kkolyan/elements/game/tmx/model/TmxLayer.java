package net.kkolyan.elements.game.tmx.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

/**
 * @author nplekhanov
 */
public class TmxLayer {

    @Attribute private String name;
    @Attribute private int width;
    @Attribute private int height;
    @Element private TmxData data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public TmxData getData() {
        return data;
    }

    public void setData(TmxData data) {
        this.data = data;
    }
}
