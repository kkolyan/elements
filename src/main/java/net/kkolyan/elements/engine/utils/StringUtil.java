package net.kkolyan.elements.engine.utils;

/**
 * @author nplekhanov
 */
public class StringUtil {
    public static String decapitalize(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return Character.toLowerCase(s.charAt(0)) + s.substring(1, s.length());
    }

    public static String capitalize(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return Character.toUpperCase(s.charAt(0)) + s.substring(1, s.length());
    }

    public static String argument(String command) {
        String[] parts = command.split("\\s", 2);
        return parts[1];
    }
}
