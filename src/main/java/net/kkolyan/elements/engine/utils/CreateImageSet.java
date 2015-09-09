package net.kkolyan.elements.engine.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nplekhanov
 */
public class CreateImageSet {
    public static void main(String[] args) throws IOException {
        createImageSet(
                "C:\\Games\\Герои 3 Дыхание Смерти\\Data",
                ".*\\.bmp",
                "D:\\dev\\elements\\src\\main\\resources\\demo2\\dirt");
    }


    public static void createImageSet(String sourceDir, String pattern, String targetPrefix) throws IOException {
        List<BufferedImage> images = new ArrayList<BufferedImage>();
        int w = 0;
        int h = 0;
        for (File file: new File(sourceDir).listFiles()) {
            if (file.getName().matches(pattern)) {
                BufferedImage image = ImageIO.read(file);
                w = Math.max(w, image.getWidth());
                h = Math.max(h, image.getHeight());
                images.add(image);
            }
        }
        BufferedImage set = new BufferedImage(w * images.size(), h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D canvas = set.createGraphics();
        for (int i = 0; i < images.size(); i ++) {
            canvas.drawImage(images.get(i), i * w, 0, null);
        }
        canvas.dispose();
        File output = new File(targetPrefix + "." + w + "x" + h + ".png");
        ImageIO.write(set, "png", output);
    }
}
