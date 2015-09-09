package net.kkolyan.elements.modeling.gait;

import net.kkolyan.elements.engine.utils.FixAlpha;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author nplekhanov
 */
public class ClipSaver {
    public static void save(Clip clip, String name, double scale) {
        try {
            clip.init();
            Dimension clipSize = clip.getSize();

            int rows = 1;
            if (Math.sqrt(clip.getLength()) * Math.sqrt(clip.getLength()) == clip.getLength()) {
                rows = (int) Math.sqrt(clip.getLength());
            }

            BufferedImage image = new BufferedImage(
                    clip.getLength() / rows * clipSize.width,
                    clipSize.height * rows,
                    BufferedImage.TYPE_INT_ARGB);

            for (int i = 0; i < clip.getLength(); i ++) {
                int row = i / rows;
                int column = i % rows;
                BufferedImage frame = image.getSubimage(
                        column * clipSize.width, row * clipSize.height,
                        clipSize.width, clipSize.height);

                Graphics2D canvas = frame.createGraphics();
                clip.updateAndRender(canvas, i);
                canvas.dispose();
            }
            image = Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, (int)(image.getWidth()*scale),(int) (image.getHeight() * scale));
//            FixAlpha.fixAlpha(image);
            File file = new File("src/main/resources/" + name + "." + (int)(clipSize.width * scale) + "x" + (int)(clipSize.height * scale) + ".png");
            file.getParentFile().mkdirs();
            ImageIO.write(image, "png", file);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
