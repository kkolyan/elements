package net.kkolyan.elements.game;

import net.kkolyan.elements.engine.core.Located;

/**
 * @author nplekhanov
 */
public interface GtaStyleControllable extends TdsControllable {
    void move(double timeDirection);
    void turn(double timeDirection);
}
