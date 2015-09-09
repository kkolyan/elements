package net.kkolyan.elements.engine.core.definition.sdl;

import net.kkolyan.elements.engine.core.definition.Node;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class SdlShell {
    private Map<String,Constructor> constructors = new HashMap<>();

    public Collection<String> getObjectNames() {
        return Collections.unmodifiableCollection(constructors.keySet());
    }

    public void loadNodes(Collection<Node> nodes) {
        for (Node node: nodes) {
            if (node.getName() == null) {
                throw new NullPointerException();
            }
            constructors.put(node.getName(), getConstructor(node));
        }
    }

    private Constructor getConstructor(String definition) {
        Constructor constructor = constructors.get(definition);
        if (constructor != null) {
            return constructor;
        }
        return new SpelBasedConstructor(definition);
    }

    private Constructor getConstructor(Node node) {
        Constructor parent = getConstructor(node.getValue());
        if (node.getChildren().isEmpty()) {
            return parent;
        }

        HierarchicalNodesConstructor constructor = new HierarchicalNodesConstructor(node.getName());
        constructor.setParent(parent);
        for (Node child: node.getChildren()) {
            Constructor childConstructor = getConstructor(child);
            if (child.getName() == null) {
                constructor.getEntries().add(childConstructor);
            } else if (child.getName().startsWith("@")) {
                constructor.getLocalDefinedParams().put(child.getName().substring(1), childConstructor);
            } else {
                constructor.getProperties().put(child.getName(), childConstructor);
            }
        }
        return constructor;
    }

    public Object createObject(String name) {
        return createObject(new Node(null, name));
    }

    public Object createObject(Node node) {
        return getConstructor(node).create(new ConstructionContext() {
            @Override
            public Object lookupValue(String name) {
                return null;
            }
        });
    }
}
