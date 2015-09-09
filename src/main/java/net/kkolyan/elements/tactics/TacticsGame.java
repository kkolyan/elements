package net.kkolyan.elements.tactics;

import net.kkolyan.elements.engine.core.Application;
import net.kkolyan.elements.engine.core.slick2d.Slick2dLauncher;
import net.kkolyan.elements.game.ElementsGameApplication;

/**
 * @author nplekhanov
 */
public class TacticsGame {

    public static void main(String[] args) {

        Application application = new ElementsGameApplication();

        Slick2dLauncher launcher = new Slick2dLauncher();
        launcher.setApplication(application);
        launcher.parseCommandBatch("tactics.cfg");
        launcher.start();
    }
}
