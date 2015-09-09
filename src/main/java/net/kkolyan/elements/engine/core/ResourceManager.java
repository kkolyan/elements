package net.kkolyan.elements.engine.core;

import net.kkolyan.elements.engine.core.templates.Vector;
import net.kkolyan.elements.engine.utils.FixAlpha;
import net.kkolyan.elements.engine.utils.RegexHelper;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class ResourceManager {
    private Map<String,ImageSet> imageSetMap = new HashMap<String, ImageSet>();
    private Map<String,SpriteSheet> spriteSheetMap = new HashMap<String, SpriteSheet>();

    public BufferedImage getImage(String imageId) {
        throw new UnsupportedOperationException();
    }

    public ImageSet getImageSet(String imageSetId) {
        ImageSet imageSet = imageSetMap.get(imageSetId);
        if (imageSet == null) {
            imageSet = loadImageSet(imageSetId);
            imageSetMap.put(imageSetId, imageSet);
        }
        return imageSet;
    }

    public SpriteSheet getSpriteSheet(String imageSetId) throws SlickException {
        SpriteSheet imageSet = spriteSheetMap.get(imageSetId);
        if (imageSet == null) {
            int[] tileSize = getTileSizeAndOrigin(imageSetId);
            imageSet = new SpriteSheet(imageSetId, tileSize[0], tileSize[1]);
            imageSet.setFilter(Image.FILTER_LINEAR);
            spriteSheetMap.put(imageSetId, imageSet);
        }
        return imageSet;
    }

    private ImageSet loadImageSet(String imageSetId) {
        try {
            return loadTiledImageSet(imageSetId);

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    // this caching gives significant boost
    private static Map<String,int[]> tileParamMap = new HashMap<String, int[]>();

    public static int[] getTileSizeAndOrigin(String imageSetId) {
        int[] tileParams = tileParamMap.get(imageSetId);
        if (tileParams == null) {
            String originSpecified = ".*\\.([0-9]+)x([0-9]+)\\.o([0-9]+)x([0-9]+).[A-z0-9]+";
            String defaultOrigin = ".*\\.([0-9]+)x([0-9]+)\\.[A-z0-9]+";
            Vector origin = null;
            List<String> parts;
            if (imageSetId.matches(originSpecified)) {
                parts = RegexHelper.find(imageSetId, originSpecified).get(0);
                origin = new Vector();
                origin.setX(Integer.parseInt(parts.get(2)));
                origin.setY(Integer.parseInt(parts.get(3)));
            } else {
                parts = RegexHelper.find(imageSetId, defaultOrigin).get(0);
            }
            int width = Integer.parseInt(parts.get(0));
            int height = Integer.parseInt(parts.get(1));
            if (origin == null) {
                origin = new Vector(width / 2, height / 2);
            }
            tileParams = new int[]{width, height, (int) origin.getX(), (int) origin.getY()};
            tileParamMap.put(imageSetId, tileParams);
        }
        return tileParams;
    }

    private ImageSet loadTiledImageSet(String imageSetId) throws IOException {
        String originSpecified = ".*\\.([0-9]+)x([0-9]+)\\.o([0-9]+)x([0-9]+).[A-z0-9]+";
        String defaultOrigin = ".*\\.([0-9]+)x([0-9]+)\\.[A-z0-9]+";

        Vector origin = null;
        List<String> parts;
        if (imageSetId.matches(originSpecified)) {
            parts = RegexHelper.find(imageSetId, originSpecified).get(0);
            origin = new Vector();
            origin.setX(Integer.parseInt(parts.get(2)));
            origin.setY(Integer.parseInt(parts.get(3)));
        } else {
            parts = RegexHelper.find(imageSetId, defaultOrigin).get(0);
        }
        int width = Integer.parseInt(parts.get(0));
        int height = Integer.parseInt(parts.get(1));

        if (origin == null) {
            origin = new Vector(width / 2, height / 2);
        }


        URL resource = Thread.currentThread().getContextClassLoader().getResource(imageSetId);
        if (resource == null) {
            throw new FileNotFoundException(imageSetId);
        }
        BufferedImage data = ImageIO.read(resource);
        if (data.getHeight() % height != 0) {
            throw new IllegalStateException("invalid imageSet: "+imageSetId);
        }
        if (data.getWidth() % width != 0) {
            throw new IllegalStateException("invalid imageSet: "+imageSetId);
        }
        int rows = data.getHeight() / height;
        int columns = data.getWidth() / width;

        TiledImageSet imageSet = new TiledImageSet();
        imageSet.setImageWidth(width);
        imageSet.setImageHeight(height);
        imageSet.setImage(data);
        imageSet.setRowCount(rows);
        imageSet.setColumnCount(columns);
        imageSet.setOrigin(origin);
        return imageSet;
    }
}
