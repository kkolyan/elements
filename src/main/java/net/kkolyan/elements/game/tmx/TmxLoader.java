package net.kkolyan.elements.game.tmx;

import net.kkolyan.elements.engine.core.Locatable;
import net.kkolyan.elements.engine.core.definition.sdl.SdlLoader;
import net.kkolyan.elements.engine.core.physics.Border;
import net.kkolyan.elements.engine.core.templates.Object2d;
import net.kkolyan.elements.engine.core.templates.Sprite;
import net.kkolyan.elements.engine.core.templates.Vector;
import net.kkolyan.elements.engine.utils.ResourcesUtil;
import net.kkolyan.elements.game.Level;
import net.kkolyan.elements.game.UniObject;
import net.kkolyan.elements.game.tmx.model.*;
import org.simpleframework.xml.core.Persister;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * https://github.com/bjorn/tiled/wiki/TMX-Map-Format
 *
 * @author nplekhanov
 */
public class TmxLoader {
    private static String[] indexToObjectName = {
            null, "EmoticonSmall", "EmoticonNormal", "EmoticonLarge", "FlyNormal", "FlyLarge"
    };

    public static Level loadTmxLevel(String resource, SdlLoader sdlLoader) {
        Level level = new Level();

        String parent = new File(resource).getParent();

        Persister persister = new Persister();
        TmxMap map;
        try {
            map = persister.read(TmxMap.class, new ByteArrayInputStream(ResourcesUtil.getResourceContent(resource)));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        if (map.getLayers().size() != 1 || !map.getLayers().get(0).getName().equals("Tiles")) {
            throw new IllegalStateException();
        }
        TmxLayer tilesLayer = map.getLayers().get(0);
        int[][] tiles = tilesLayer.getData().getGidMatrix();
        for (int x = 0; x < tilesLayer.getWidth(); x ++) {
            for (int y = 0; y < tilesLayer.getHeight(); y ++) {
                Sprite sprite = new Sprite();
                int gid = tiles[y][x];
                if (gid == 0) {
                    continue;
                }
                TmxTileSet tileSet = map.getTileSetByGid(gid);
                int imageIndex = gid - tileSet.getFirstgid();
                if (imageIndex < 0) {
                    throw new IllegalStateException();
                }
                sprite.setImageSetId(new File(parent, tileSet.getImage().getSource()).toString()+"#"+(imageIndex));
                sprite.setDepth(100000);
                sprite.setX(x * map.getTilewidth() + map.getTilewidth()/2);
                sprite.setY(y * map.getTileheight() + map.getTileheight()/2);
                level.getObjects().add(sprite);
            }
        }
        for (TmxObjectGroup objectGroup: map.getObjectGroups()) {
            if (objectGroup.getObjects() == null) {
                continue;
            }
            for (TmxObject object: objectGroup.getObjects()) {
                Integer gid = object.getGid();
                if (gid != null) {
                    TmxTileSet tileSet = map.getTileSetByGid(gid);

                    int index = gid - tileSet.getFirstgid();
                    if (index == 0) {
                        level.getStartPosition().set(object.getX() + 16, object.getY() + 16);
                        level.getStartPosition().setDirection(Double.parseDouble(object.getProperty("direction", "0")));
                    } else {
                        if (index < 0 || index >= indexToObjectName.length) {
                            throw new IllegalStateException();
                        }
                        String objectName = indexToObjectName[index];

                        Object o = sdlLoader.createObject(objectName);
                        Locatable locatable = optionalLocatable(o);
                        if (locatable != null) {
                            locatable.set(new Vector(object.getX() + 16, object.getY() + 16));
                        }
                        level.getObjects().add(o);
                    }
                } else {
                    List<Vector> polyLine = object.getPolyline().getPoints();
                    Vector offset = new Vector(object.getX(), object.getY());
                    for (int i = 1; i < polyLine.size(); i ++) {
                        Vector begin = polyLine.get(i - 1).getTranslated(offset);
                        Vector end = polyLine.get(i).getTranslated(offset);
                        level.getObjects().add(new Border(begin, end));
                    }
                }
            }
        }
        return level;
    }

    private static Locatable optionalLocatable(Object o) {
        if (o instanceof Locatable) {
            return (Locatable) o;
        }
        if (o instanceof UniObject) {
            if (((UniObject) o).is(Locatable.class)) {
                return ((UniObject) o).as(Locatable.class);
            }
        }
        return null;
    }
}
