package net.kkolyan.elements.engine.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author nplekhanov
 */
public class ResourcesUtil {
    public static byte[] getResourceContent(String resource) {
        try {
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            if (stream == null) {
                return null;
            }
            try {
                return getStreamContent(stream);
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }

    private static byte[] getStreamContent(InputStream stream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        while (true) {
            int n = stream.read(b);
            if (n < 0) {
                break;
            }
            bos.write(b, 0, n);
        }
        return bos.toByteArray();
    }
}
