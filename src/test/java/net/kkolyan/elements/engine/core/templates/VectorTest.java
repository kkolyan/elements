package net.kkolyan.elements.engine.core.templates;

import org.junit.Test;

import static java.lang.Math.*;
import static org.junit.Assert.*;

/**
 * @author nplekhanov
 */
public class VectorTest {
    static final double epsilon = .00001;

    @Test
    public void testCos() {
        Vector a = new Vector(10, 0);
        assertEquals(1.0, a.cos(new Vector(10, 0)), epsilon);
        assertEquals(cos(PI / 4), a.cos(new Vector(10, 10)), epsilon);
        assertEquals(.0, a.cos(new Vector(0, 10)), epsilon);
        assertEquals(cos(3 * PI / 4), a.cos(new Vector(-10, 10)), epsilon);
        assertEquals(-1.0, a.cos(new Vector(-10, 0)), epsilon);
    }

    @Test
    public void testProjection0() {
        Vector a = new Vector(10, 10);
        Vector b = new Vector(5, 0);
        Vector c = a.getProjectionOf(b);

        assertEquals(2.5, c.getX(), epsilon);
        assertEquals(2.5, c.getY(), epsilon);
    }

    @Test
    public void testProjection1() {
        Vector a = new Vector(10, 10);
        Vector b = new Vector(-5, 0);
        Vector c = a.getProjectionOf(b);

        assertEquals(-2.5, c.getX(), epsilon);
        assertEquals(-2.5, c.getY(), epsilon);
    }
}
