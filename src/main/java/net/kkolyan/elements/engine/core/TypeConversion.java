package net.kkolyan.elements.engine.core;

import java.awt.Dimension;
import java.math.BigDecimal;
import java.util.Locale;

/**
 * @author nplekhanov
 */
public class TypeConversion {
    static {
        Locale.setDefault(Locale.UK);
    }
    public static Object convert(Object value, Class<?> type) {
        if (value == null) {
            if (type.isPrimitive()) {
                if (type == boolean.class) {
                    return false;
                }
                return 0;
            }
            return null;
        }
        if (type.isInstance(value)) {
            return value;
        }
        if (type == Dimension.class) {
            String[] parts = value.toString().trim().split(",");
            return new Dimension(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]));
        }
        if (type == String.class) {
            return value.toString();
        }
        if (type.isPrimitive()) {
            if (type == boolean.class) return Boolean.parseBoolean(value.toString());

            Number n;
            if (value instanceof Number) {
                n = (Number) value;
            } else {
                n = new BigDecimal(value.toString());
            }
            if (type == byte.class) return n.byteValue();
            if (type == short.class) return n.shortValue();
            if (type == int.class) return n.intValue();
            if (type == long.class) return n.longValue();
            if (type == float.class) return n.floatValue();
            if (type == double.class) return n.doubleValue();
        }
        if (type.isEnum()) {
            return Enum.valueOf((Class)type, (String) value);
        }
        throw new IllegalStateException("can't convert "+value+" to "+type);
    }

}
