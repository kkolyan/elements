package net.kkolyan.elements.engine.core.graphics;

import net.kkolyan.elements.engine.core.Located;
import net.kkolyan.elements.engine.core.Oriented;

/**
 * @author nplekhanov
 */
public interface Drawable extends Located, Oriented {
    double getDepth();

    double getScale();

    String getImageSetId();

    double getFrameIndex();

    void setFrameIndex(double frameIndex);

    double getFrameRate();

    void handleAnimationEnd();
}
