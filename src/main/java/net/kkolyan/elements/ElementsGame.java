package net.kkolyan.elements;

import net.kkolyan.elements.engine.core.Application;
import net.kkolyan.elements.engine.core.slick2d.Slick2dLauncher;
import net.kkolyan.elements.game.ElementsGameApplication;

/**
 * @author nplekhanov
 */
public class ElementsGame {

    public static void main(String[] args) {

        Application application = new ElementsGameApplication();

        Slick2dLauncher launcher = new Slick2dLauncher();
        launcher.setApplication(application);
        launcher.parseCommandBatch("config.cfg");
        launcher.start();
    }
}
