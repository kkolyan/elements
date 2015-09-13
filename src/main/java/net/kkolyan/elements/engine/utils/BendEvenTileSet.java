package net.kkolyan.elements.engine.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author nplekhanov
 */
public class BendEvenTileSet {
    public static void main(String[] args) throws IOException {
        bend(
                "D:\\dev\\elements.git\\src\\main\\resources\\tactics\\grdrt.128x128.png",
                "D:\\dev\\elements.git\\src\\main\\resources\\tactics\\grdrt2.128x128.png");
    }
    public static void bend(String sourceFile, String targetFile) throws IOException {
        BufferedImage source = ImageIO.read(new File(sourceFile));

        BufferedImage target = new BufferedImage(source.getWidth() / 2, source.getHeight() * 2, BufferedImage.TYPE_INT_ARGB);
        Graphics2D canvas = target.createGraphics();
        canvas.drawImage(source, 0, 0, null);
        canvas.drawImage(source, -source.getWidth() / 2, source.getHeight(), null);
        canvas.dispose();

        ImageIO.write(target, "png", new File(targetFile));
    }
}
