package net.kkolyan.elements.game;


import java.util.*;

/**
 * @author nplekhanov
 */
public class CompoundUniObject implements UniObject {
    private Collection<Object> components = Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
    private Map<Class,Collection<?>> lookupCache;
    private boolean initialized;

    protected void initCompoundMultiObject() {
    }

    public void addComponents(Object...components) {
        Collections.addAll(this.components, components);
    }

    private Collection<Object> getComponents() {
        if (!initialized) {
            initialized = true;
            initCompoundMultiObject();
        }
        return components;
    }

    private <T> Collection<T> lookup(Class<T> type) {
        if (lookupCache == null) {
            lookupCache = new LinkedHashMap<>(getComponents().size());
        }
        @SuppressWarnings("unchecked")
        Collection<T> value = (Collection<T>) lookupCache.get(type);
        if (value == null) {
            value = Collections.newSetFromMap(new IdentityHashMap<T, Boolean>());
            for (Object candidate: getComponents()) {
                if (type.isInstance(candidate)) {
                    value.add(type.cast(candidate));
                }
                if (candidate != this && candidate instanceof UniObject) {
                    value.addAll(((UniObject) candidate).of(type));
                }
            }

//            lookupCache.put(type, value);
        }
        return value;
    }

    @Override
    public <T> T as(Class<T> type) {
        Collection<T> o = lookup(type);
        if (o.size() == 1) {
            return type.cast(o.iterator().next());
        }
        throw new IllegalStateException("can't found single object of type "+type+" among components: "+ components+". found for type: "+o);
    }

    @Override
    public <T> Collection<T> of(Class<T> type) {
        return lookup(type);
    }

    @Override
    public boolean is(Class<?> type) {
        return !lookup(type).isEmpty();
    }
}
