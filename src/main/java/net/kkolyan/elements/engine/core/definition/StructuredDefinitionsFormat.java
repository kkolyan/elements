package net.kkolyan.elements.engine.core.definition;

import net.kkolyan.elements.engine.utils.RegexHelper;

import java.io.*;
import java.util.*;

/**
 * @author nplekhanov
 */
public class StructuredDefinitionsFormat {
    public static void formatDefinitions(List<Node> nodes, OutputStream outputStream) {
        for (Node node: nodes) {
            formatDefinition(node, outputStream);
        }
    }

    public static void formatDefinition(Node node, OutputStream outputStream) {
        formatDefinition("", node, outputStream);
    }

    private static void formatDefinition(String indent, Node node, OutputStream outputStream) {
        String s = indent;
        if (node.getName() == null) {
            s += node.getValue();
        } else {
            s += node.getName() + " = " + node.getValue();
        }
        s += "\r\n";
        try {
            outputStream.write(s.getBytes("utf8"));
            outputStream.flush();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        for (Node child: node.getChildren()) {
            formatDefinition(indent + "    ", child, outputStream);
        }
    }

    public static List<Node> parseDefinitions(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int n = 0;
        Deque<Frame> stack = new ArrayDeque<Frame>();
        List<Node> rootNodes = new ArrayList<Node>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.trim().isEmpty() && !line.trim().startsWith("#")) {
                String indent = getIndent(line);
                String[] parts = line.trim().split("=");
                Node node = new Node();
                if (parts.length == 1) {
                    node.setValue(parts[0].trim());
                } else if (parts.length == 2) {
                    node.setName(parts[0].trim());
                    node.setValue(parts[1].trim());
                } else {
                    throw new IllegalStateException("invalid line: "+n);
                }

                while (true) {
                    if (stack.isEmpty()) {
                        rootNodes.add(node);
                        stack.push(new Frame(node, indent));
                        break;
                    }
                    int indentDelta = compareIndents(indent, stack.peek().getIndent());
                    if (indentDelta > 0) {
                        stack.peek().getNode().getChildren().add(node);
                        stack.push(new Frame(node, indent));
                        break;
                    }
                    stack.remove();
                }
            }
            n ++;
        }
        return rootNodes;
    }

    private static class Frame {
        private Node node;
        private String indent;

        private Frame(Node node, String indent) {
            this.node = node;
            this.indent = indent;
        }

        public Node getNode() {
            return node;
        }

        public String getIndent() {
            return indent;
        }
    }

    private static int compareIndents(String a, String b) {
        if (a.equals(b)) {
            return 0;
        }
        if (a.startsWith(b)) {
            return 1;
        }
        if (b.startsWith(a)) {
            return -1;
        }
        throw new IllegalStateException("inconsistent indents: "+ Arrays.toString(a.toCharArray()) +" and "+ Arrays.toString(b.toCharArray()));
    }

    private static String getIndent(String line) {
        return RegexHelper.find(line, "^( *).*$").get(0).get(0);
    }
}
