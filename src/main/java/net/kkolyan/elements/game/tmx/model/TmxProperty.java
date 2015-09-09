package net.kkolyan.elements.game.tmx.model;

import org.simpleframework.xml.Attribute;

/**
 * @author nplekhanov
 */
public class TmxProperty {
    @Attribute private String name;
    @Attribute private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
