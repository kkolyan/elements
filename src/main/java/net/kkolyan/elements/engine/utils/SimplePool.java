package net.kkolyan.elements.engine.utils;

import java.util.ArrayDeque;

/**
 * @author nplekhanov
 */
public class SimplePool<T> implements Pool<T> {
    private ArrayDeque<T> pool = new ArrayDeque<>();
    private ObjectProvider<T> initializer;

    public SimplePool(ObjectProvider<T> initializer) {
        this.initializer = initializer;
    }

    @Override
    public T borrow() {
        T t = pool.poll();
        if (t == null) {
            t = initializer.getObject();
        }
        return t;
    }

    @Override
    public void release(T o) {
        pool.offer(o);
    }
}
