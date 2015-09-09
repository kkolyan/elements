package net.kkolyan.elements.engine.utils;

import net.kkolyan.elements.engine.core.templates.Vector;

/**
 * @author nplekhanov
 */
public class AngleDemo {
    public static void main(String[] args) {
        for (int i = 0; i < 360; i += 30) {
            System.out.println(Vector.fromAngle(i, 1));
        }
        for (int i = -180; i < 180; i += 30) {
            System.out.println(i+","+Vector.fromAngle(i, 1).angle());
        }
    }
}
