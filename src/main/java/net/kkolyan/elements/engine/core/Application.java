package net.kkolyan.elements.engine.core;

import net.kkolyan.elements.engine.core.graphics.Curve;
import net.kkolyan.elements.engine.core.graphics.Drawable;
import net.kkolyan.elements.engine.core.graphics.Rectangle;
import net.kkolyan.elements.engine.core.graphics.Text;

import java.util.Collection;
import java.util.List;

/**
 * @author nplekhanov
 */
public interface Application {

    void handleTick(ControllerContext context);

    Collection<? extends Text> getScreenText();

    Collection<? extends Text> getWorldText();

    List<? extends Drawable> getWorldSprites();

    Located getViewPortCenter();

    List<? extends Curve> getWorldCurves();

    double getViewPortScale();

    List<? extends Rectangle> getWorldRectangles();

}
