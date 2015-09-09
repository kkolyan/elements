package net.kkolyan.elements.engine.utils;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.*;

/**
 * @author nplekhanov
 */
public class IconGenerator {
    public static void main(String[] args) throws Exception {
        File dir = new File("target/IconGenerator");
        dir.mkdirs();
        Collection<Color> colors = new ArrayList<>();
        for (Field field: Color.class.getFields()) {
            if (Modifier.isStatic(field.getModifiers()) && field.getType() == Color.class && field.getName().equals(field.getName().toLowerCase())) {
                colors.add((Color) field.get(null));
            }
        }
        Collection<Shape> shapes = new ArrayList<>();
        shapes.add(new Ellipse2D.Float(4, 4, 24, 24));
        shapes.add(new Rectangle(6,6,20,20));
        shapes.add(createTriangle(16, 12, -90));
        List<BufferedImage> icons = new ArrayList<>();
        for (Color fillColor: colors) {
            for (Color frameColor: colors) {
                if (fillColor == frameColor) {
                    continue;
                }
                for (Shape shape: shapes) {
                    BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D canvas = icon.createGraphics();
                    canvas.setBackground(new Color(0,0,0,0));
                    try {
                        RenderingHints rh = new RenderingHints(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                        canvas.setRenderingHints(rh);
                        canvas.setStroke(new BasicStroke(5));
                        canvas.setColor(fillColor);
                        canvas.fill(shape);
                        canvas.setColor(frameColor);
                        canvas.draw(shape);
                    } finally {
                        canvas.dispose();
                    }
                    icons.add(icon);
                }
            }
        }
        Collections.shuffle(icons);
        int cols = (int) ceil(sqrt(icons.size()));
        int rows = icons.size() / cols;

        BufferedImage image = new BufferedImage(32 * cols, 32 * rows, BufferedImage.TYPE_INT_ARGB);
        Graphics2D canvas = image.createGraphics();
        canvas.setBackground(new Color(0,0,0,0));
        try {
            int i = 0;
            for (BufferedImage icon: icons) {
                int col = i % cols;
                int row = i / cols;
                int x = col * 32;
                int y = row * 32;
                canvas.drawImage(icon, x, y, null);
                i ++;
            }
        } finally {
            canvas.dispose();
        }
        ImageIO.write(image, "png", new File(dir, "icons.32x32.png"));

    }

    private static Shape createTriangle(int offset, int size, double rotationDegrees) {
        double[] angles = {0, 120, -120};
        int[] xpoints = new int[3];
        int[] ypoints = new int[3];
        for (int i = 0; i < angles.length; i ++) {
            xpoints[i] = (int) (offset + size * cos((angles[i] + rotationDegrees) * PI / 180));
            ypoints[i] = (int) (offset + size * sin((angles[i] + rotationDegrees) * PI / 180));
        }
        return new Polygon(xpoints, ypoints, 3);
    }
}
