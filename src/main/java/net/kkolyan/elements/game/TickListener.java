package net.kkolyan.elements.game;

import net.kkolyan.elements.engine.core.ControllerContext;

/**
 * @author nplekhanov
 */
public interface TickListener {
    void beforeTick(ControllerContext context);
    void afterTick(ControllerContext context);
}
