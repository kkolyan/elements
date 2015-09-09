package net.kkolyan.elements.engine.utils;

/**
 * @author nplekhanov
 */
public interface Function<S,T> {
    T apply(S s);
}
