package net.kkolyan.elements.engine.core.templates;

import net.kkolyan.elements.engine.core.Application;
import net.kkolyan.elements.engine.core.ControllerContext;
import net.kkolyan.elements.engine.core.Located;
import net.kkolyan.elements.engine.core.graphics.Curve;
import net.kkolyan.elements.engine.core.graphics.Drawable;
import net.kkolyan.elements.engine.core.graphics.Rectangle;
import net.kkolyan.elements.engine.core.graphics.Text;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author nplekhanov
 */
public class BaseApplication implements Application {

    @Override
    public void handleTick(ControllerContext context) {
    }

    @Override
    public Collection<? extends Text> getScreenText() {
        return Collections.emptyList();
    }

    @Override
    public Collection<? extends Text> getWorldText() {
        return Collections.emptyList();
    }

    @Override
    public List<? extends Drawable> getWorldSprites() {
        return Collections.emptyList();
    }

    @Override
    public Located getViewPortCenter() {
        return new Vector();
    }

    @Override
    public List<? extends Curve> getWorldCurves() {
        return Collections.emptyList();
    }

    @Override
    public double getViewPortScale() {
        return 1;
    }

    @Override
    public List<? extends Rectangle> getWorldRectangles() {
        return Collections.emptyList();
    }
}
