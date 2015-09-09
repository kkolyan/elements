package net.kkolyan.elements.engine.core.slick2d;

/**
 * @author nplekhanov
 */
public interface InputHandler {

    void handleTyping(char letter);

    void handleCommand(String command);

    void handleMousePosition(int x, int y);

    void handleMeasurableCommand(String command, int change);
}
