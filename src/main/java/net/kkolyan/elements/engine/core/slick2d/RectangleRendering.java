package net.kkolyan.elements.engine.core.slick2d;

import net.kkolyan.elements.engine.core.Application;
import net.kkolyan.elements.engine.core.graphics.Rectangle;
import net.kkolyan.elements.engine.utils.Function;
import net.kkolyan.elements.engine.utils.Profiling;
import net.kkolyan.elements.engine.utils.StopWatch;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.List;

/**
 * @author nplekhanov
 */
public class RectangleRendering {
    public static void drawRectangles(Application application, Graphics canvas,
            GameContainer container, Function<String, Color> colors) {


        StopWatch renderLineWatch = Profiling.startStopWatch("render/drawLine");
        try {
            for (Rectangle rect: application.getWorldRectangles()) {

                canvas.translate(container.getWidth() / 2, container.getHeight() / 2);

                float scale = (float) (1.0 / application.getViewPortScale());
                canvas.scale(scale, scale);

                canvas.translate((float)-application.getViewPortCenter().getX(),(float) -application.getViewPortCenter().getY());

                int x1 = (int) (rect.getX() - rect.getSize().getX() / 2);
                int y1 = (int) (rect.getY() - rect.getSize().getY() / 2);
                int w = (int) (rect.getSize().getX());
                int h = (int) (rect.getSize().getY());

                canvas.setColor(colors.apply(rect.getFrameColor()));
                canvas.drawRect(x1, y1, w, h);

                canvas.setColor(colors.apply(rect.getFillColor()));
                canvas.fillRect(x1, y1, w, h);

                canvas.resetTransform();
            }
        } finally {
            renderLineWatch.stop();
        }
    }
}
