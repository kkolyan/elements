package net.kkolyan.elements.engine.core.definition.sdl;

import net.kkolyan.elements.engine.core.Reflection;

import java.util.*;

/**
 * @author nplekhanov
 */
public class HierarchicalNodesConstructor implements Constructor {

    private String name;

    private Constructor parent;
    private Map<String, Constructor> properties = new HashMap<>();
    private Map<String, Constructor> localDefinedParams = new HashMap<>();

    private List<Constructor> entries = new ArrayList<>();

    public void setParent(Constructor parent) {
        this.parent = parent;
    }

    public Map<String, Constructor> getProperties() {
        return properties;
    }

    public List<Constructor> getEntries() {
        return entries;
    }

    public Map<String, Constructor> getLocalDefinedParams() {
        return localDefinedParams;
    }

    public HierarchicalNodesConstructor(String name) {
        this.name = name;
    }

    @Override
    public Object create(final ConstructionContext callerContext) {
        ConstructionContext mergedContext = new MergedConstructionContext(callerContext);

        Object o = parent.create(mergedContext);

        for (Map.Entry<String, Constructor> property: properties.entrySet()) {
            Object value = property.getValue().create(mergedContext);
            if (o instanceof Map) {
                ((Map)o).put(property.getKey(), value);
            } else {
                Reflection.setProperty(o, property.getKey(), value);
            }
        }
        for (Constructor entry: entries) {
            Object value = entry.create(mergedContext);
            Collection asCollection = (Collection) o;
            asCollection.add(value);
        }
        return o;
    }

    public String getName() {
        return name;
    }

    private class MergedConstructionContext implements ConstructionContext {
        private final ConstructionContext callerContext;

        public MergedConstructionContext(ConstructionContext callerContext) {
            this.callerContext = callerContext;
        }

        @Override
        public Object lookupValue(String name) {
            Object o = callerContext.lookupValue(name);
            if (o != null) {
                return o;
            }
            Constructor constructor = localDefinedParams.get(name);
            if (constructor != null) {
                return constructor.create(callerContext);
            }
            return null;
        }

        @Override
        public String toString() {
            return "MergedContext{" +
                    name +
                    '}';
        }
    }
}
