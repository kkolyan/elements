package net.kkolyan.elements.game;

import net.kkolyan.elements.engine.core.templates.Ray;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author nplekhanov
 */
public class Level {
    private Collection<Object> players = new ArrayList<>();
    private Collection<Object> objects = new ArrayList<>();

    public Collection<Object> getPlayers() {
        return players;
    }

    public Collection<Object> getObjects() {
        return objects;
    }
}
