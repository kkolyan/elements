package net.kkolyan.elements.engine.core.slick2d.render;

import net.kkolyan.elements.engine.core.Application;
import net.kkolyan.elements.engine.core.graphics.Text;
import net.kkolyan.elements.engine.core.templates.Vector;
import net.kkolyan.elements.engine.utils.Function;
import net.kkolyan.elements.engine.utils.Profiling;
import net.kkolyan.elements.engine.utils.StopWatch;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import java.awt.Font;

/**
 * @author nplekhanov
 */
public class TextRendering {


    private static TrueTypeFont font;

    public static void drawTexts(Application scene, Graphics canvas, GameContainer container, Function<String,Color> colors) {

        StopWatch renderTextWatch = Profiling.startStopWatch("render/drawString");
        try {
            if (font == null) {
                font = new TrueTypeFont(new Font("Courier New", Font.PLAIN, 12), false);
            }
            canvas.setFont(font);

            for (Text text: scene.getScreenText()) {
                Color color = colors.apply(text.getColor());

                canvas.setColor(color);
                int i = 0;
                for (String line: text.getLines()) {
                    int x = (int) text.getX();
                    int y = (int) (text.getY() + (i++) * 12);

                    canvas.drawString(line, x, y);
                }
            }

            canvas.translate(container.getWidth()/2, container.getHeight()/2);

            float scale = (float) (1.0 / scene.getViewPortScale());
            canvas.scale(scale, scale);

            canvas.translate((float)-scene.getViewPortCenter().getX(),(float) -scene.getViewPortCenter().getY());
            for (Text text: scene.getWorldText()) {
                Color color = colors.apply(text.getColor());

                canvas.setColor(color);
                int i = 0;
                for (String line: text.getLines()) {
                    int x = (int) text.getX();
                    int y = (int) (text.getY() + (i++) * 12);

                    int width = font.getWidth(line);
                    canvas.drawString(line, x - width/2, y);
                }
            }
            canvas.resetTransform();
            canvas.setColor(Color.white);
        } finally {
            renderTextWatch.stop();
        }
    }
}
