package net.kkolyan.elements.engine.core.slick2d;

import org.newdawn.slick.Game;

/**
 * @author nplekhanov
 */
public interface SwitchableInputGame extends Game {

    void setAcceptingInput(boolean acceptingInput);
}
