package net.kkolyan.elements.engine.utils;

/**
 * @author nplekhanov
 */
public interface Pool<T> {
    T borrow();
    void release(T o);
}
