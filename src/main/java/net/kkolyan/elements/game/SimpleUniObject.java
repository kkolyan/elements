package net.kkolyan.elements.game;

import java.util.Collection;
import java.util.Collections;

/**
 * @author nplekhanov
 */
public class SimpleUniObject implements UniObject {
    private Object object;

    public SimpleUniObject() {
    }

    public SimpleUniObject(Object object) {
        this.object = object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public <T> T as(Class<T> type) {
        return type.cast(object);
    }

    @Override
    public <T> Collection<T> of(Class<T> type) {
        if (is(type)) {
            return Collections.singletonList(as(type));
        }
        return Collections.emptyList();
    }

    @Override
    public boolean is(Class<?> type) {
        return type.isInstance(object);
    }
}
