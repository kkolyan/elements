package net.kkolyan.elements.game.tmx.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.core.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author nplekhanov
 */
@Root
public class TmxMap {

    @Attribute private String version;
    @Attribute private String orientation;
    @Attribute private String renderorder;
    @Attribute private int width;
    @Attribute private int height;
    @Attribute private int tilewidth;
    @Attribute private int tileheight;
    @ElementList(inline = true, entry = "tileset") private List<TmxTileSet> tileSets;
    @ElementList(inline = true, entry = "layer") private List<TmxLayer> layers;
    @ElementList(inline = true, entry = "objectgroup") private List<TmxObjectGroup> objectGroups;

    @Validate
    public void validate() {
        if (!version.equals("1.0")) {
            throw new IllegalStateException();
        }
        if (!orientation.equals("orthogonal")) {
            throw new IllegalStateException();
        }
        if (!renderorder.equals("right-down")) {
            throw new IllegalStateException();
        }
        List<TmxTileSet> sortedTileSets = new ArrayList<>(tileSets);

        Collections.sort(sortedTileSets, new Comparator<TmxTileSet>() {
            @Override
            public int compare(TmxTileSet o1, TmxTileSet o2) {
                return Integer.compare(o1.getFirstgid(), o2.getFirstgid());
            }
        });
        if (!sortedTileSets.equals(tileSets)) {
            throw new IllegalStateException();
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getRenderorder() {
        return renderorder;
    }

    public void setRenderorder(String renderorder) {
        this.renderorder = renderorder;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTilewidth() {
        return tilewidth;
    }

    public void setTilewidth(int tilewidth) {
        this.tilewidth = tilewidth;
    }

    public int getTileheight() {
        return tileheight;
    }

    public void setTileheight(int tileheight) {
        this.tileheight = tileheight;
    }

    public List<TmxTileSet> getTileSets() {
        return tileSets;
    }

    public void setTileSets(List<TmxTileSet> tileSets) {
        this.tileSets = tileSets;
    }

    public List<TmxLayer> getLayers() {
        return layers;
    }

    public void setLayers(List<TmxLayer> layers) {
        this.layers = layers;
    }

    public List<TmxObjectGroup> getObjectGroups() {
        return objectGroups;
    }

    public void setObjectGroups(List<TmxObjectGroup> objectGroups) {
        this.objectGroups = objectGroups;
    }

    public TmxTileSet getTileSetByGid(int gid) {
        List<TmxTileSet> ts = getTileSets();
        for (int i = ts.size() - 1; i >= 0; i--) {
            TmxTileSet tileSet = ts.get(i);
            if (tileSet.getFirstgid() <= gid) {
                return tileSet;
            }
        }
        throw new IllegalStateException();
    }
}
