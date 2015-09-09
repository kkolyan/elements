package net.kkolyan.elements.modeling.gait;

import java.awt.*;

/**
 * @author nplekhanov
 */
public interface Clip {

    void init() throws Exception;

    int getLength();

    void setScale(double scale);

    void updateAndRender(Graphics2D canvas, int frameIndex);

    Dimension getSize();
}
