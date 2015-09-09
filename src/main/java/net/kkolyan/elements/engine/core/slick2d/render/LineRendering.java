package net.kkolyan.elements.engine.core.slick2d.render;

import net.kkolyan.elements.engine.core.Application;
import net.kkolyan.elements.engine.core.Located;
import net.kkolyan.elements.engine.core.graphics.Curve;
import net.kkolyan.elements.engine.utils.Profiling;
import net.kkolyan.elements.engine.utils.StopWatch;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.List;

/**
 * @author nplekhanov
 */
public class LineRendering {
    public static void drawLines(Application application, Graphics canvas, GameContainer container) {


        StopWatch renderLineWatch = Profiling.startStopWatch("render/drawLine");
        try {
            for (Curve curve: application.getWorldCurves()) {
                List<? extends Located> points = curve.getPoints();
                Located prev = points.get(0);
                for (int i = 1; i < points.size(); i++) {
                    Located point = points.get(i);

                    canvas.setColor(Color.white);
                    canvas.translate(container.getWidth() / 2, container.getHeight() / 2);

                    float scale = (float) (1.0 / application.getViewPortScale());
                    canvas.scale(scale, scale);

                    canvas.translate((float)-application.getViewPortCenter().getX(),(float) -application.getViewPortCenter().getY());

                    int x1 = (int) prev.getX();
                    int y1 = (int) prev.getY();
                    int x2 = (int) point.getX();
                    int y2 = (int) point.getY();
                    canvas.setLineWidth(curve.getWidth());
                    canvas.drawLine(x1, y1, x2, y2);

                    canvas.resetTransform();

                    prev = point;
                }
            }
        } finally {
            renderLineWatch.stop();
        }
    }
}
