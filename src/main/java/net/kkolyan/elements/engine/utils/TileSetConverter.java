package net.kkolyan.elements.engine.utils;

import net.kkolyan.elements.engine.core.ResourceManager;
import net.kkolyan.elements.engine.core.graphics.Drawable;
import net.kkolyan.elements.engine.core.templates.Sprite;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author nplekhanov
 */
public class TileSetConverter {
    public static void convert(File input, File output) throws FileNotFoundException, UnsupportedEncodingException {
        Scanner scanner = new Scanner(input);
        PrintStream writer = new PrintStream(output, "utf8");
        try {
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

            for (int i = 0; i < indexes.size(); i++) {
                writer.println("Tile");
                int x = firstFrame.getWidth() * (i % w) - 1024;
                int y = firstFrame.getHeight() * (i / w) - 1024;
                Integer index = indexes.get(i);
                writer.println("\tx = " + x);
                writer.println("\ty = " + y);
                writer.println("\timageSetId = " + imageSetId + "#" + index);
                writer.flush();
            }
        } finally {
            writer.close();
        }
    }

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        File dir = new File("D:\\dev\\elements\\src\\main\\resources\\game");
        convert(new File(dir, "demo2.tiles.txt"), new File(dir, "demo2.tiles"));
    }
}
