package net.kkolyan.elements.engine.utils;

import net.kkolyan.elements.engine.core.templates.Object2d;
import net.kkolyan.elements.engine.core.templates.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * @author nplekhanov
 */
public class AreaFiller<T extends Object2d> implements ObjectProvider<Collection<?>> {
    private ObjectProvider<T> initializer;
    private double fillAreaRadius;
    private Vector fillAreaCenter = new Vector();
    private int count;

    public void setFillAreaCenterValues(double[] values) {
        fillAreaCenter.set(values[0], values[1]);
    }

    public Vector getFillAreaCenter() {
        return fillAreaCenter;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setFillAreaCenter(Vector fillAreaCenter) {
        this.fillAreaCenter = fillAreaCenter;
    }

    public void setInitializer(ObjectProvider<T> initializer) {
        this.initializer = initializer;
    }

    public void setFillAreaRadius(double fillAreaRadius) {
        this.fillAreaRadius = fillAreaRadius;
    }


    @Override
    public Collection<?> getObject() {
        List<T> list = new ArrayList<T>();
        Random random = new Random();
        for (int i = 0; i < count; i ++) {
            T o = initializer.getObject();
            o.transpose(fillAreaCenter);
            o.transpose(Vector.fromAngle(random.nextDouble() * 360.0, Math.sqrt(random.nextDouble()) * fillAreaRadius));
            list.add(o);
        }
        return list;
    }
}
