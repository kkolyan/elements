package net.kkolyan.elements.engine.utils;

/**
 * @author nplekhanov
 */
public class ResourceNameDemo {
    public static void main(String[] args) {
        String regex = ".*\\.([0-9]+)x([0-9]+)\\.[A-z0-9]+";
        String regex2 = ".*\\.([0-9]+)x([0-9]+)\\.o([0-9]+)x([0-9]+).[A-z0-9]+";
        System.out.println(RegexHelper.find("tower.64x64.o24x32.png", regex));
        System.out.println(RegexHelper.find("tower_fire.96x64.o24x32.png", regex2));

        System.out.println("tower.64x64.o24x32.png".matches(regex));
        System.out.println("tower.64x64.o24x32.png".matches(regex2));
    }
}
