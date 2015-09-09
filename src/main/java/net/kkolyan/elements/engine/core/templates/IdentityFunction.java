package net.kkolyan.elements.engine.core.templates;

import net.kkolyan.elements.engine.utils.Function;

/**
 * @author nplekhanov
 */
public class IdentityFunction<T> implements Function<T,T> {
    @Override
    public T apply(T t) {
        return t;
    }
}
