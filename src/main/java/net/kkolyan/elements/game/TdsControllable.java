package net.kkolyan.elements.game;

import net.kkolyan.elements.engine.core.Located;

/**
 * @author nplekhanov
 */
public interface TdsControllable extends UniObject {
    void setTarget(Located target);
    Object shot();
}
