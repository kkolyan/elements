package net.kkolyan.elements.engine.core;

import net.kkolyan.elements.engine.core.templates.Vector;

import java.awt.image.BufferedImage;

/**
 * @author nplekhanov
 */
public interface ImageSet {

    BufferedImage getFrame(int index);

    int getImageCount();

    Vector getOrigin();
}
