package net.kkolyan.elements.engine.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author nplekhanov
 */
public class FixAlpha {


    public static final int ALPHA_THRESHOLD = 120;

    public static void main(String[] args) throws Exception {
        FixAlpha.fixAlpha("D:\\dev\\elements\\src\\main\\resources\\tactics\\marine.28x28.png");
        FixAlpha.fixAlpha("D:\\dev\\elements\\src\\main\\resources\\tactics\\marine_fire.50x28.o14x14.png");
        FixAlpha.fixAlpha("D:\\dev\\elements\\src\\main\\resources\\tactics\\cv_move.64x34.o24x17.png");
    }

    public static void fixAlpha(String file) throws IOException {
        File f = new File(file);
        BufferedImage in = ImageIO.read(f);

        fixAlpha(in);

        ImageIO.write(in, "png", f);
    }

    public static void fixAlpha(BufferedImage image) {
        for (int i = 0; i < image.getWidth() * image.getHeight(); i ++) {
            int x = i % image.getWidth();
            int y = i / image.getWidth();
            Color color = new Color(image.getRGB(x, y), true);
            int alpha = color.getAlpha();
            if (alpha < ALPHA_THRESHOLD) {
                int[] colors = {0, 0,0 };
                int n = 0;
                for (int j = 0; j < 9; j ++) {
                    int px = x + j % 3 - 1;
                    int py = y + j / 3 - 1;

                    if (px < 0 || px >= image.getWidth()) {
                        continue;
                    }
                    if (py < 0 || py >= image.getHeight()) {
                        continue;
                    }
                    Color pc = new Color(image.getRGB(px, py), true);
                    if (pc.getAlpha() < ALPHA_THRESHOLD) {
                        continue;
                    }
                    colors[0] += pc.getRed();
                    colors[1] += pc.getGreen();
                    colors[2] += pc.getBlue();
                    n ++;
                }
                if (n > 0) {
                    image.setRGB(x, y, new Color(colors[0]/n, colors[1]/n, colors[2]/n, 0).getRGB());
                }
            }
        }
    }
}
