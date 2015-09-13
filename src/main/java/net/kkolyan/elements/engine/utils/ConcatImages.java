package net.kkolyan.elements.engine.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nplekhanov
 */
public class ConcatImages {
    public static void main(String[] args) throws IOException {
        List<String> sourceFiles = Arrays.asList(
                "tactics/marine.28x28.png",
                "tactics/moto.64x64.png",
                "tactics/cv.64x34.png",
                "game/tank.64x64.png");
        concatImages(sourceFiles, "D:\\dev\\elements.git\\src\\main\\resources\\tactics\\player_palette.png");
    }
    public static void concatImages(Collection<String> inputResources, String targetFile) throws IOException {
        List<BufferedImage> images = inputResources.stream().map(o -> Thread.currentThread().getContextClassLoader().getResource(o)).map(o -> {
            try {
                return ImageIO.read(o);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }).collect(Collectors.toList());

        int width = images.stream().mapToInt(RenderedImage::getWidth).max().getAsInt();
        int height = images.stream().mapToInt(RenderedImage::getHeight).max().getAsInt();

        BufferedImage targetImage = new BufferedImage(width * images.size(), height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D canvas = targetImage.createGraphics();

        for (int i = 0; i < images.size(); i++) {
            BufferedImage image = images.get(i);
            canvas.drawImage(image, width * i + (width - image.getWidth()) / 2 , (height - image.getHeight()) / 2, null);
        }
        canvas.dispose();

        ImageIO.write(targetImage, "png", new File(targetFile));
    }
}
