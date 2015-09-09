package net.kkolyan.elements.engine.core.graphics;

import net.kkolyan.elements.engine.core.Located;

/**
 * @author nplekhanov
 */
public interface Rectangle extends Located {
    Located getSize();
    String getFillColor();
    String getFrameColor();
}
