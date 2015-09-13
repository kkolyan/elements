package net.kkolyan.elements.engine.utils;

import java.io.*;

/**
 * @author nplekhanov
 */
public class IOUtils {
    public static String readResourceContent(String resource, String charset) {
        try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)) {
            if (stream == null) {
                throw new FileNotFoundException("can't find classpath resource: "+resource);
            }
            return readStreamContent(stream, charset);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String readStreamContent(InputStream stream, String charset) throws IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        byte[] bytes = new byte[1024];
        while (true) {
            int n = stream.read(bytes);
            if (n < 0) {
                break;
            }
            buffer.write(bytes, 0, n);
        }

        return buffer.toString(charset);
    }

    public static String readFileContent(File file, String charset) {
        try (InputStream stream = new BufferedInputStream(new FileInputStream(file))) {
            return readStreamContent(stream, charset);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
