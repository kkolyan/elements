package net.kkolyan.elements.engine.core.definition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author nplekhanov
 */
public class Node {
    private String name;
    private String value;
    private List<Node> children = new ArrayList<Node>(0);

    public Node() {
    }

    public Node(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public List<Node> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        String s;
        if (name != null) {
            s = name + " = " + value;
        } else {
            s = value;
        }
        return "Node{" + s + ", " + children.size() + " children}";
    }

    public Node getChild(String name) {
        List<Node> candidates = new ArrayList<>();
        for (Node node: getChildren()) {
            if (name.equals(node.getName())) {
                candidates.add(node);
            }
        }
        if (candidates.size() != 1) {
            throw new IllegalStateException("can't find unique child by name '"+name+"'. found: "+candidates);
        }
        return candidates.get(0);
    }

    public String getNameOrValue() {
        if (name != null) {
            return name;
        }
        return value;
    }
}
