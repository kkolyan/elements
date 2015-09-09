package net.kkolyan.elements.engine.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * @author nplekhanov
 */
public class Ini {
    @SuppressWarnings("unchecked")
    public static Map<String,String> getIni(String resource) {

        Properties properties = new Properties();
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        try {
            try {
                properties.load(inputStream);
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return (Map) properties;
    }
}
