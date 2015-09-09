package net.kkolyan.elements.engine.core.definition.sdl;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SpelBasedConstructorTest {

    @Test
    public void test1() {
        Object the2 = new SpelBasedConstructor("2").create(new SimpleConstructionContext());
        assertEquals(2, the2);

        Map<String, Object> map = new HashMap<>();
        map.put("size", the2);
        Object result = new SpelBasedConstructor("100 * size * size * size").create(new SimpleConstructionContext(map));
        assertEquals(800, result);
    }
}