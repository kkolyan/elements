package net.kkolyan.elements.engine.core;

import net.kkolyan.elements.engine.core.templates.Vector;
import net.kkolyan.elements.engine.utils.Function;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class Debug {
    private static boolean enabled = false;
    private static List<Rectangle> rectangles = new ArrayList<Rectangle>();
    private static List<Line> lines = new ArrayList<Line>();
    private static List<Ellipse> ellipses = new ArrayList<>();

    public static void setEnabled(boolean enabled) {
        Debug.enabled = enabled;
    }

    public static void drawRectangle(Located center, Located size, String color) {
        if (!enabled) {
            return;
        }
        rectangles.add(new Rectangle(center, size, color));
    }

    public static void drawEllipse(Located center, Located size, String color) {
        if (!enabled) {
            return;
        }
        ellipses.add(new Ellipse(center, size, color));
    }

    public static void drawLine(Located begin, Located end, String color) {
        if (!enabled) {
            return;
        }
        lines.add(new Line(begin, end, color));
    }

    public static boolean isEnabled() {
        return enabled;
    }

    private static class Ellipse {

        private final Located center;
        private final Located size;
        private final String color;

        public Ellipse(Located center, Located size, String color) {
            this.center = center;
            this.size = size;
            this.color = color;
        }
    }

    private static class Rectangle {

        private final Located center;
        private final Located size;
        private final String color;

        public Rectangle(Located center, Located size, String color) {
            this.center = center;
            this.size = size;
            this.color = color;
        }
    }

    private static class Line {
        private Located begin;
        private Located end;
        private final String color;

        private Line(Located begin, Located end, String color) {
            this.begin = begin;
            this.end = end;
            this.color = color;
        }
    }

    public static void draw(Application scene, GameContainer container, Graphics canvas, Function<String,Color> colors) throws SlickException {
        if (!enabled) {
            return;
        }
        float scale = (float) (1.0 / scene.getViewPortScale());
        for (Rectangle o : rectangles) {
            Color color = colors.apply(o.color);
            canvas.setColor(color);
            canvas.translate(container.getWidth() / 2, container.getHeight() / 2);
            canvas.scale(scale, scale);
            canvas.translate((float) -scene.getViewPortCenter().getX(), (float) -scene.getViewPortCenter().getY());
            canvas.translate((float) o.center.getX(), (float) o.center.getY());

            canvas.translate((float) -o.size.getX() / 2, (float) -o.size.getY() / 2);
            canvas.drawRect(0, 0, (float) o.size.getX(), (float) o.size.getY());
            canvas.resetTransform();
        }
        for (Line o: lines) {
            Color color = colors.apply(o.color);
            canvas.setColor(color);
            canvas.translate(container.getWidth() / 2, container.getHeight() / 2);
            canvas.scale(scale, scale);
            canvas.translate((float) -scene.getViewPortCenter().getX(), (float) -scene.getViewPortCenter().getY());
            canvas.translate((float) o.begin.getX(), (float) o.begin.getY());

            canvas.drawLine(0, 0, (float) (o.end.getX() - o.begin.getX()), (float) (o.end.getY() - o.begin.getY()));
            canvas.resetTransform();
        }
        for (Ellipse o: ellipses) {
            Color color = colors.apply(o.color);
            canvas.setColor(color);
            canvas.translate(container.getWidth() / 2, container.getHeight() / 2);
            canvas.scale(scale, scale);
            canvas.translate((float) -scene.getViewPortCenter().getX(), (float) -scene.getViewPortCenter().getY());
            canvas.translate((float) o.center.getX(), (float) o.center.getY());

            canvas.translate((float) -o.size.getX() / 2, (float) -o.size.getY() / 2);
            canvas.drawOval(0, 0, (float) o.size.getX(), (float) o.size.getY());
            canvas.resetTransform();
        }
    }

    public static void clear() {
        ellipses.clear();
        rectangles.clear();
        lines.clear();
    }
}
