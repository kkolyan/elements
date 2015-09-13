package net.kkolyan.elements.engine.utils;

import net.kkolyan.elements.engine.core.ImageSet;
import net.kkolyan.elements.engine.core.ResourceManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author nplekhanov
 */
public class CreateTilesPalette {
    public static void main(String[] args) throws IOException {
        createPalette("tactics/grdrt.128x128.png", "D:\\dev\\elements.git\\src\\main\\resources\\tactics\\grdrt_palette.128x128.png", false);
    }

    public static void createPalette(String imageSetId, String targetFile, boolean markup) throws IOException {
        ResourceManager resourceManager = new ResourceManager();
        ImageSet imageSet = resourceManager.getImageSet(imageSetId);
        int width = (int) Math.ceil(Math.sqrt(imageSet.getImageCount()));
        int w = 0;
        int h = 0;
        for (int i = 0; i < imageSet.getImageCount(); i ++) {
            BufferedImage image = imageSet.getFrame(i);
            w = Math.max(w, image.getWidth());
            h = Math.max(h, image.getHeight());
        }
        BufferedImage palette = new BufferedImage(w * width, h * width, BufferedImage.TYPE_INT_ARGB);
        Graphics2D canvas = palette.createGraphics();
        for (int i = 0; i < imageSet.getImageCount(); i ++) {
            int x = w * (i % width);
            int y = h * (i / width);
            canvas.setColor(Color.white);
            canvas.setFont(new Font("Courier", Font.BOLD, 20));
            canvas.drawImage(imageSet.getFrame(i), x, y, null);
            if (markup) {
                canvas.drawRect(x, y, w, h);
                canvas.drawString(String.valueOf((char)('a' + i)), x + 10, y + 20);
            }
        }
        canvas.dispose();
        ImageIO.write(palette, "png", new File(targetFile));
    }
}
