package net.kkolyan.elements.engine.core;

import net.kkolyan.elements.engine.core.slick2d.Slick2dLauncher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author nplekhanov
 */
public class Reflection {
    public static void setProperty(Object o, String property, Object value) {
        try {
            Method setter = getSetter(o.getClass(), property);
            setter.invoke(o, TypeConversion.convert(value, setter.getParameterTypes()[0]));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Method getSetter(Class owner, String property) throws NoSuchMethodException {
        String methodName = "set" + property;
        for (Method method: owner.getMethods()) {
            if (method.getName().equalsIgnoreCase(methodName)) {
                return method;
            }
        }
        throw new IllegalStateException("can't find setter for "+owner.getCanonicalName()+"."+property);
    }

    public static Method getGetter(Class owner, String property) throws NoSuchMethodException {
        for (Method method: owner.getMethods()) {
            if (method.getParameterTypes().length > 0) {
                continue;
            }
            if (method.getName().equalsIgnoreCase("get"+property)) {
                return method;
            }
            if (method.getName().equalsIgnoreCase("is"+property) && method.getReturnType() == boolean.class) {
                return method;
            }
        }
        throw new IllegalStateException("can't find getter for "+owner.getCanonicalName()+"."+property);
    }

    public static Object getProperty(Object o, String property) {
        try {
            Method setter = getGetter(o.getClass(), property);
            return setter.invoke(o);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
}
