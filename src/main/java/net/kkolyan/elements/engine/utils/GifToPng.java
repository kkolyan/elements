package net.kkolyan.elements.engine.utils;

import org.w3c.dom.NamedNodeMap;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nplekhanov
 */
public class GifToPng {
    public static void main(String[] args) {
        convert("game/acute.gif", "target/GifToPng");
    }

    public static List<BufferedImage> getFrames(Object gif) throws IOException {
        ArrayList<BufferedImage> frames = new ArrayList<BufferedImage>();
        ImageReader ir = ImageIO.getImageReadersByFormatName("gif").next();
        ir.setInput(ImageIO.createImageInputStream(gif));
        int w = 0;
        int h = 0;
        BufferedImage buffer = null;

        for(int i = 0; i < ir.getNumImages(true); i++) {
            IIOMetadata metadata = ir.getImageMetadata(i);
            NamedNodeMap imageDescriptor = ((IIOMetadataNode) metadata.getAsTree("javax_imageio_gif_image_1.0")).getElementsByTagName("ImageDescriptor").item(0).getAttributes();
            NamedNodeMap graphicControlExtension = ((IIOMetadataNode) metadata.getAsTree("javax_imageio_gif_image_1.0")).getElementsByTagName("GraphicControlExtension").item(0).getAttributes();
            int imageWidth = Integer.parseInt(imageDescriptor.getNamedItem("imageWidth").getNodeValue());
            int imageHeight = Integer.parseInt(imageDescriptor.getNamedItem("imageHeight").getNodeValue());
            int imageLeftPosition = Integer.parseInt(imageDescriptor.getNamedItem("imageLeftPosition").getNodeValue());
            int imageTopPosition = Integer.parseInt(imageDescriptor.getNamedItem("imageTopPosition").getNodeValue());
            String disposalMethod = graphicControlExtension.getNamedItem("disposalMethod").getNodeValue();
            if (i == 0) {
                w = imageWidth;
                h = imageHeight;
                buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            }
            BufferedImage source = ir.read(i);
            buffer.getGraphics().drawImage(source, imageLeftPosition, imageTopPosition, null);

            BufferedImage destination = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            destination.getGraphics().drawImage(buffer, 0, 0, null);
            frames.add(destination);


            if (disposalMethod.equals("restoreToBackgroundColor")) {
                buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            } else if (disposalMethod.equals("doNotDispose")) {
                //
            }
        }
        return frames;
    }

    public static void convert(String resource, String outputDirectory) {
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            try {
                List<BufferedImage> frames = getFrames(inputStream);
                int w = frames.get(0).getWidth();
                int h = frames.get(0).getHeight();
                BufferedImage image = new BufferedImage(w * frames.size(), h, BufferedImage.TYPE_INT_ARGB);
                Graphics canvas = image.getGraphics();

                int n = 0;
                for (BufferedImage frame: frames) {
                    canvas.drawImage(frame, w * (n++), 0, null);
                }
                String[] parts = resource.split("[\\.\\\\/]");

                File output = new File(outputDirectory, parts[parts.length - 2] + "." + w + "x" + h + ".png");
                output.getParentFile().mkdirs();
                ImageIO.write(image, "png", output);
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
