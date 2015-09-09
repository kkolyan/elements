package net.kkolyan.elements.engine.core.grid;

import net.kkolyan.elements.engine.core.Located;
import net.kkolyan.elements.engine.core.templates.Object2d;
import net.kkolyan.elements.engine.core.templates.Vector;
import net.kkolyan.elements.engine.utils.CollectionUtil;
import net.kkolyan.elements.engine.utils.Function;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author nplekhanov
 */
public class GridTest {

    @Test
    public void test1() {
        double[][] expected = {
                {-15, -5},
                {-5, -5},
                {5, -5},
                {-15, -15},
                {-5, -15},
                {5, -15},
        };
        test(new Unit(-3, -18), 10, expected);
    }

    @Test
    public void test2() {
        double[][] expected = {
                {-5, -15},
                {5, -15},
        };
        test(new Unit(-3, -18), 5, expected);
    }

    @Test
    public void test3() {
        double[][] expected = {
                {-5, -15},
                {5, -15},
                {-5, -5},
                {5, -5},
                {15, -5},
                {-5, 5},
                {5, 5},
                {15,5},
        };

        Grid<Unit> grid = prepare();
        Collection<Unit> a = grid.lookupAtLongArea(new Unit(-3, -18), new Unit(8, 2), 5);
        assertResultMatches(expected, a);
    }

    private Grid<Unit> prepare() {

        Grid<Unit> grid = new Grid<Unit>(10, new Function<Unit, Located>() {
            @Override
            public Located apply(Unit unit) {
                return unit;
            }
        });
        for (int x = 0; x < 4; x ++ ) {
            for (int y = 0; y < 4; y ++ ) {
                grid.add(new Unit(-15 + x * 10, -15 + y * 10));
            }
        }
        grid.rebuild();

        return grid;
    }

    private void test(Unit point, double distance, double[][] expectedUnits) {

        Grid<Unit> grid = prepare();

        Collection<Unit> a = grid.lookup(point, new Vector(distance * 2, distance * 2));

        assertResultMatches(expectedUnits, a);
    }

    private static void assertResultMatches(double[][] expectedUnits, Collection<Unit> actual) {
        List<Unit> expected = new ArrayList<Unit>();
        for (double[] unit: expectedUnits) {
            expected.add(new Unit(unit[0], unit[1]));
        }
        assertContentEquals(expected, actual);
    }

    private static void assertContentEquals(List<?> expected, Collection<?> actual) {
        Function<Object, String> toString = new Function<Object, String>() {
            @Override
            public String apply(Object o) {
                return o.toString();
            }
        };

        List<String> exp = new ArrayList<String>(CollectionUtil.transform(expected, toString));
        List<String> act = new ArrayList<String>(CollectionUtil.transform(new ArrayList<Object>(actual), toString));

        Collections.sort(exp);
        Collections.sort(act);

        Assert.assertEquals(CollectionUtil.join(exp, "\n"),CollectionUtil.join(act, "\n"));
    }

    private static class Unit extends Object2d {
        private Unit(double x, double y) {
            set(x, y);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Unit)) {
                return false;
            }
            Unit o = (Unit) obj;
            return getX() == o.getX() && getY() == o.getY();
        }

        @Override
        public String toString() {
            return "{"+getX()+","+getY()+"}";
        }
    }
}
