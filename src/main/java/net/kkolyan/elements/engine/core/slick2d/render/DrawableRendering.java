package net.kkolyan.elements.engine.core.slick2d.render;

import net.kkolyan.elements.engine.core.Application;
import net.kkolyan.elements.engine.core.ResourceManager;
import net.kkolyan.elements.engine.core.graphics.Drawable;
import net.kkolyan.elements.engine.core.templates.Vector;
import net.kkolyan.elements.engine.utils.NumberUtils;
import net.kkolyan.elements.engine.utils.Profiling;
import net.kkolyan.elements.engine.utils.StopWatch;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author nplekhanov
 */
public class DrawableRendering {

    private static void draw(Application scene, Drawable sprite, Graphics canvas, GameContainer container, ResourceManager resourceManager) throws SlickException {

        if (sprite.getImageSetId() == null) {
            return;
        }

        StopWatch resWatch = Profiling.startStopWatch("render/draw/resources");
        SpriteSheet imageSet;
        int[] tileParams;
        Image image;
        try {
            tileParams = ResourceManager.getTileSizeAndOrigin(sprite.getImageSetId());
            if (sprite.getImageSetId().contains("#")) {
                imageSet = resourceManager.getSpriteSheet(sprite.getImageSetId().substring(0, sprite.getImageSetId().indexOf("#")));

                int index = Integer.parseInt(sprite.getImageSetId().substring(sprite.getImageSetId().indexOf("#") + 1));
                image = imageSet.getSprite(index % imageSet.getHorizontalCount(), index / imageSet.getHorizontalCount());
            } else {
                imageSet = resourceManager.getSpriteSheet(sprite.getImageSetId());

                int index = (int) Math.round(getNormalizedIndex(imageSet.getHorizontalCount() * imageSet.getVerticalCount(), sprite.getFrameIndex()));

                image = imageSet.getSprite(index % imageSet.getHorizontalCount(), index / imageSet.getHorizontalCount());
            }
        } finally {
            resWatch.stop();
        }


        StopWatch transformWatch = Profiling.startStopWatch("render/draw/transform");
        try {
            canvas.translate(container.getWidth()/2, container.getHeight()/2);

            float scale = (float) (1.0 / scene.getViewPortScale());
            canvas.scale(scale, scale);

            canvas.translate((float)-scene.getViewPortCenter().getX(),(float) -scene.getViewPortCenter().getY());
            canvas.translate((float)sprite.getX(), (float)sprite.getY());
            canvas.scale((float)sprite.getScale(), (float)sprite.getScale());
            canvas.rotate(0,0,(float)sprite.getDirection());

            Vector origin = new Vector(tileParams[2], tileParams[3]);
            canvas.translate((float)-origin.getX(), (float)-origin.getY());
        } finally {
            transformWatch.stop();
        }


        StopWatch drawingWatch = Profiling.startStopWatch("render/draw/drawImage");
        try {
            canvas.drawImage(image, 0, 0);
        } finally {
            drawingWatch.stop();
        }
        canvas.resetTransform();

        double nextFrame = getNormalizedIndex(imageSet.getHorizontalCount() * imageSet.getVerticalCount(), sprite.getFrameIndex() + sprite.getFrameRate());
        double lastFrame = sprite.getFrameIndex();
        sprite.setFrameIndex(nextFrame);
        if (lastFrame > nextFrame) {
            sprite.handleAnimationEnd();
        }
    }

    private static double getNormalizedIndex(int limit, double index) {
        double i = index;
        while (Math.round(i) < 0) {
            i += limit;
        }
        while (Math.round(i) >= limit) {
            i -= limit;
        }
        return i;
    }

    public static void drawDrawables(
            Application application,
            Graphics canvas,
            GameContainer container,
            ResourceManager resourceManager) throws SlickException {

        List<? extends Drawable> sprites = application.getWorldSprites();

        Profiling.addNumber("sprites", sprites.size());

        StopWatch sortingWatch = Profiling.startStopWatch("render/sorting");
        try {
            Collections.sort(sprites, new Comparator<Drawable>() {
                @Override
                public int compare(Drawable o1, Drawable o2) {
                    int diff = -Double.compare(o1.getDepth(), o2.getDepth());
                    if (diff == 0) {
                        diff = NumberUtils.compare(System.identityHashCode(o1), System.identityHashCode(o2));
                    }
                    return diff;
                }
            });
        } finally {
            sortingWatch.stop();
        }

        StopWatch renderDrawWatch = Profiling.startStopWatch("render/draw");
        try {
            for (Drawable sprite: sprites) {
                DrawableRendering.draw(application, sprite, canvas, container, resourceManager);
            }
        } finally {
            renderDrawWatch.stop();
        }
    }
}
