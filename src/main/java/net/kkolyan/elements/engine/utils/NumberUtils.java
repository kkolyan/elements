package net.kkolyan.elements.engine.utils;

/**
 * @author nplekhanov
 */
public class NumberUtils {
    public static int compare(int a, int b) {
        if (a > b) {
            return 1;
        }
        if(a < b) {
            return -1;
        }
        return 0;
    }
}
