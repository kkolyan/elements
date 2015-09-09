package net.kkolyan.elements.game;

import java.util.Collection;
import java.util.List;

/**
 * @author nplekhanov
 */
public class UniObjects {
    public static <T> Collection<T> projectRequiredComponents(Collection<? extends UniObject> objects, Class<T> componentType) {
        return new RequiredComponentsProjection<>(objects, componentType);
    }

    public static <T, C extends Collection<? super T>> C extractComponents(Collection<? extends UniObject> objects, Class<T> componentType, C extracted) {
        for (UniObject o: objects) {
            extracted.addAll(o.of(componentType));
        }
        return extracted;
    }
}
