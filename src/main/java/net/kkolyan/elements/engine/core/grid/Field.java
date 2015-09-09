package net.kkolyan.elements.engine.core.grid;

import net.kkolyan.elements.engine.core.Located;

import java.util.Collection;
import java.util.List;

/**
 * @author nplekhanov
 */
public interface Field<T> {

    List<T> lookup(Located areaCenter, Located areaSize);

    List<T> lookupAtLongArea(Located longAreaBegin, Located longAreaEnd, double thickness);

    void rebuild();

    void add(T o);

    void update(T t);

    int getObjectsCount();

    void clear();
}
