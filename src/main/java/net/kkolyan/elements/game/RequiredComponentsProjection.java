package net.kkolyan.elements.game;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author nplekhanov
 */
public class RequiredComponentsProjection<T> extends AbstractCollection<T> {
    private Collection<? extends UniObject> objects;
    private Class<T> type;

    public RequiredComponentsProjection(Collection<? extends UniObject> objects, Class<T> type) {
        this.objects = objects;
        this.type = type;
    }

    @Override
    public Iterator<T> iterator() {
        final Iterator<? extends UniObject> it = objects.iterator();
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public T next() {
                return it.next().as(type);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public int size() {
        return objects.size();
    }
}
