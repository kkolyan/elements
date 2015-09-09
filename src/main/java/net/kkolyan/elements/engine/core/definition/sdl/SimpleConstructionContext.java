package net.kkolyan.elements.engine.core.definition.sdl;

import java.util.Collections;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class SimpleConstructionContext implements ConstructionContext {
    private Map<String,?> map;

    public SimpleConstructionContext(Map<String, ?> map) {
        this.map = map;
    }

    public SimpleConstructionContext() {
        this(Collections.<String, Object>emptyMap());
    }

    @Override
    public Object lookupValue(String name) {
        return map.get(name);
    }
}
