package net.kkolyan.elements.game.tmx;

import net.kkolyan.elements.engine.core.Locatable;
import net.kkolyan.elements.engine.core.definition.sdl.SdlLoader;
import net.kkolyan.elements.engine.core.physics.Border;
import net.kkolyan.elements.engine.core.templates.Object2d;
import net.kkolyan.elements.engine.core.templates.Ray;
import net.kkolyan.elements.engine.core.templates.Sprite;
import net.kkolyan.elements.engine.core.templates.Vector;
import net.kkolyan.elements.engine.utils.ResourcesUtil;
import net.kkolyan.elements.game.Level;
import net.kkolyan.elements.game.UniObject;
import net.kkolyan.elements.game.tmx.model.*;
import net.kkolyan.elements.tactics.Surface;
import org.simpleframework.xml.core.Persister;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.*;

/**
 * https://github.com/bjorn/tiled/wiki/TMX-Map-Format
 *
 * @author nplekhanov
 */
public class TmxLoader {

    public static Level loadTmxLevel(String resource, SdlLoader sdlLoader) {
        Map<String,List<String>> tmxMapping = (Map) sdlLoader.createObject("tmxMapping");
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
        List<String> tileNames = tmxMapping.get("Tiles");
        TmxLayer tilesLayer = map.getLayers().get(0);
        int[][] tiles = tilesLayer.getData().getGidMatrix();
        for (int x = 0; x < tilesLayer.getWidth(); x ++) {
            for (int y = 0; y < tilesLayer.getHeight(); y ++) {
                int gid = tiles[y][x];
                if (gid == 0) {
                    continue;
                }
                TmxTileSet tileSet = map.getTileSetByGid(gid);
                int imageIndex = gid - tileSet.getFirstgid();
                if (imageIndex < 0) {
                    throw new IllegalStateException();
                }
                String tileName = tileNames.get(gid);
                Surface tile = (Surface) sdlLoader.createObject(tileName);
                tile.setImageSetId(new File(parent, tileSet.getImage().getSource()).toString() + "#" + (imageIndex));
                tile.setX(x * map.getTilewidth() + map.getTilewidth()/2);
                tile.setY(y * map.getTileheight() + map.getTileheight()/2);
                level.getObjects().add(tile);
            }
        }
        for (TmxObjectGroup objectGroup: map.getObjectGroups()) {
            if (objectGroup.getObjects() == null) {
                continue;
            }
            List<String> objectNames = tmxMapping.get(objectGroup.getName());
            for (TmxObject object: objectGroup.getObjects()) {
                Integer gid = object.getGid();
                if (gid != null) {
                    TmxTileSet tileSet = map.getTileSetByGid(gid);

                    int index = gid - tileSet.getFirstgid();
                    if (index < 0 || index >= objectNames.size()) {
                        throw new IllegalStateException();
                    }
                    String objectName = objectNames.get(index);

                    Object o = sdlLoader.createObject(objectName);
                    Locatable locatable = optionalLocatable(o);
                    if (locatable != null) {
                        locatable.set(new Vector(object.getX() + 16, object.getY() + 16));
                    }
                    if (objectGroup.getName().equals("PlayerUnits")) {
                        level.getPlayers().add(o);
                    } else {
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
