package net.kkolyan.elements.game.tmx.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.util.Collection;

/**
 * @author nplekhanov
 */
public class TmxObjectGroup {

    @Attribute private String name;
    @ElementList(inline = true, entry = "object", required = false) private Collection<TmxObject> objects;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<TmxObject> getObjects() {
        return objects;
    }

    public void setObjects(Collection<TmxObject> objects) {
        this.objects = objects;
    }
}
