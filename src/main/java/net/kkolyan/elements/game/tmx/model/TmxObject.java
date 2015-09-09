package net.kkolyan.elements.game.tmx.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Transient;

import java.util.List;

/**
 * @author nplekhanov
 */
public class TmxObject {

    @Attribute(required = false) private Integer gid;
    @Attribute private double x;
    @Attribute private double y;
    @Element(required = false) private TmxPolyLine polyline;
    @ElementList(name = "properties", entry = "property", required = false) private List<TmxProperty> properties;

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public TmxPolyLine getPolyline() {
        return polyline;
    }

    public void setPolyline(TmxPolyLine polyline) {
        this.polyline = polyline;
    }

    public List<TmxProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<TmxProperty> properties) {
        this.properties = properties;
    }

    public String getProperty(String name, String defaultValue) {
        if (properties != null) {
            for (TmxProperty property: properties) {
                if (property.getName().equals(name)) {
                    return property.getValue();
                }
            }
        }
        return defaultValue;
    }
}
