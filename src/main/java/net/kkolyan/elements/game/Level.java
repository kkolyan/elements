package net.kkolyan.elements.game;

import net.kkolyan.elements.engine.core.templates.Ray;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author nplekhanov
 */
public class Level {
    private Collection<Ray> startPositions = new ArrayList<>();
    private Collection<Object> objects = new ArrayList<>();

    public Collection<Ray> getStartPositions() {
        return startPositions;
    }

    public Collection<Object> getObjects() {
        return objects;
    }
}
