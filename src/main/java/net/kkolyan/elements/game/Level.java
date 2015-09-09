package net.kkolyan.elements.game;

import net.kkolyan.elements.engine.core.templates.Ray;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author nplekhanov
 */
public class Level {
    private Ray startPosition = new Ray();
    private Collection<Object> objects = new ArrayList<>();

    public Ray getStartPosition() {
        return startPosition;
    }

    public Collection<Object> getObjects() {
        return objects;
    }

    public void setObjects(Collection<Object> objects) {
        this.objects = objects;
    }

    public void setStartPosition(Ray startPosition) {
        this.startPosition = startPosition;
    }
}
