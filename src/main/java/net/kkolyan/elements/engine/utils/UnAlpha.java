package net.kkolyan.elements.engine.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author nplekhanov
 */
public class UnAlpha {
    public static void main(String[] args) throws IOException {
        File file = new File("D:\\dev\\elements\\src\\main\\resources\\game\\fly-high-wait.256x256.png");
        BufferedImage image = ImageIO.read(file);
        for (int i = 0; i < image.getWidth() * image.getHeight(); i ++) {
            int x = i % image.getWidth();
            int y = i / image.getWidth();
            Color color = new Color(image.getRGB(x, y), true);
            int alpha = color.getAlpha();
            if (alpha > 10) {
                alpha = 255;
            }
            image.setRGB(x, y, new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB());
        }
        ImageIO.write(image, "png", file);
    }
}
