package net.kkolyan.elements.game.tmx.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

/**
 * @author nplekhanov
 */
public class TmxTileSet {
    @Attribute private int firstgid;
    @Attribute private String name;
    @Attribute private int tilewidth;
    @Attribute private int tileheight;
    @Element private TmxImage image;

    public int getFirstgid() {
        return firstgid;
    }

    public void setFirstgid(int firstgid) {
        this.firstgid = firstgid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public TmxImage getImage() {
        return image;
    }

    public void setImage(TmxImage image) {
        this.image = image;
    }
}
