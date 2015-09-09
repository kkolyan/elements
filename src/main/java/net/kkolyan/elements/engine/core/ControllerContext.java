package net.kkolyan.elements.engine.core;

import net.kkolyan.elements.engine.core.templates.Vector;

import java.util.Set;

/**
 * @author nplekhanov
 */
public interface ControllerContext {

    double getTickLength();

    boolean isCommandActive(String command);

    int getMeasurableCommandValue(String command);

    Vector getWorldMousePosition();

    Located getScreenSize();

    String getTypedCharacters();

    Set<String> getCommands();

    void setMouseCursor(String imageSetId);
}
