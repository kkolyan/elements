package net.kkolyan.elements.engine.utils;

import net.kkolyan.elements.engine.core.ResourceManager;
import net.kkolyan.elements.engine.core.graphics.Drawable;
import net.kkolyan.elements.engine.core.templates.Sprite;
import net.kkolyan.elements.engine.core.templates.Vector;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * @author nplekhanov
 */
public class TilesFactory implements ObjectProvider<Collection<?>> {
    private String definition;
    private double depth = 1000;
    private Vector offset;

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setOffset(Vector offset) {
        this.offset = offset;
    }

    @Override
    public Collection<?> getObject() {

        Scanner scanner = new Scanner(Thread.currentThread().getContextClassLoader().getResourceAsStream(definition));
        String imageSetId = scanner.nextLine();
        if (!scanner.nextLine().isEmpty()) {
            throw new IllegalStateException();
        }

        int w = 0;
        int h = 0;

        List<Integer> indexes = new ArrayList<Integer>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().replace(" ","");
            w = Math.max(w, line.length());
            h ++;
            for (char c: line.toCharArray()) {
                indexes.add(c - 'a');
            }
        }

        ResourceManager resourceManager = new ResourceManager();
        BufferedImage firstFrame = resourceManager.getImageSet(imageSetId).getFrame(0);

        List<Drawable> tiles = new ArrayList<Drawable>();
        for (int i = 0; i < indexes.size(); i++) {
            Integer index = indexes.get(i);
            Sprite sprite = new Sprite();
            sprite.setDepth(depth);
            sprite.setImageSetId(imageSetId);
            sprite.setFrameRate(0);
            sprite.setFrameIndex(index);
            sprite.transpose(offset);
            sprite.transpose(firstFrame.getWidth() * (i % w), firstFrame.getHeight() * (i / w));
            tiles.add(sprite);
        }
        return tiles;
    }
}
