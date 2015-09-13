package net.kkolyan.elements.tactics;

import net.kkolyan.elements.engine.core.Located;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class SurfaceMap {
    private Map<String,SurfaceType> map = new HashMap<>();
    private int resolution = 128;

    public void addSurface(Surface surface) {
        Object last = map.put(key(surface), surface.getType());
        if (last != null) {
            throw new IllegalStateException("duplicated surface object");
        }
    }
    public SurfaceType getSurfaceType(Located p) {
        return map.get(key(p));
    }

    private String key(Located p) {
        return snap(p.getX()) +", "+snap(p.getY());
    }

    private int snap(double v) {
        return ((int) v) / resolution * resolution;
    }
}
