package net.kkolyan.elements.engine.core;

import net.kkolyan.elements.engine.core.templates.Vector;

import java.awt.image.BufferedImage;

/**
 * @author nplekhanov
 */
public class TiledImageSet implements ImageSet {
    private BufferedImage image;
    private int rowCount;
    private int columnCount;
    private int imageWidth;
    private int imageHeight;
    private Vector origin;

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    @Override
    public BufferedImage getFrame(int index) {
        int row = index / getColumnCount();
        int column = index % getColumnCount();

        int x = column * imageWidth;
        int y = row * imageHeight;

        return getImage().getSubimage(x, y, imageWidth, imageHeight);
    }

    @Override
    public int getImageCount() {
        return getColumnCount() * getRowCount();
    }

    public void setOrigin(Vector origin) {
        this.origin = origin;
    }

    @Override
    public Vector getOrigin() {
        return origin;
    }
}
