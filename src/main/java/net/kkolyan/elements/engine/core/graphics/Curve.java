package net.kkolyan.elements.engine.core.graphics;

import net.kkolyan.elements.engine.core.Located;

import java.util.List;

/**
 * @author nplekhanov
 */
public interface Curve {
    List<? extends Located> getPoints();
    int getWidth();
}
