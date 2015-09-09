package net.kkolyan.elements.engine.utils;

import net.kkolyan.elements.engine.core.Located;
import net.kkolyan.elements.engine.core.templates.Vector;

import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * @author nplekhanov
 */
public class ObjectsGenerator {
    public static void generate(String def, int count, Located areaCenter, Located areaSize) {
        Locale.setDefault(Locale.UK);
        Random random = new Random();
        for (int i = 0; i < count; i ++) {
            System.out.println(def);
            System.out.printf("\t\tx = %.3f\n", areaCenter.getX() + random.nextDouble() * areaSize.getX() - areaSize.getX() / 2);
            System.out.printf("\t\ty = %.3f\n", areaCenter.getY() + random.nextDouble() * areaSize.getY() - areaSize.getY() / 2);
        }
    }
/*

        NPC
            body = EmoticonNormal
                x = -668.020
                y = -1370.954
 */
    public static void main(String[] args) {
        generate("NPC\n" +
                "\tbody = EmoticonNormal", 60, new Vector(0, 0), new Vector(1024, 1024));
        generate("NPC\n" +
                "\tbody = EmoticonLarge", 10, new Vector(0, 0), new Vector(1024, 1024));
    }
}
