package net.kkolyan.elements.game;

import java.util.Collection;

/**
 * @author nplekhanov
 */
public interface UniObject {

    boolean is(Class<?> type);

    <T> T as(Class<T> type);

    <T> Collection<T> of(Class<T> type);
}
