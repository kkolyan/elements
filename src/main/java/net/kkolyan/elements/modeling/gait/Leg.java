package net.kkolyan.elements.modeling.gait;

import net.kkolyan.elements.engine.core.templates.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nplekhanov
 */
public class Leg {
    private List<Node> nodes = new ArrayList<>();
    private double position;

    public Leg() {
    }

    public Leg(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public double getPosition() {
        return position;
    }

    public Leg setPosition(double position) {
        this.position = position;
        return this;
    }

    public Leg getMultiplied(double factor) {
        return getMultiplied(factor, factor);
    }

    public Leg getMultiplied(double xFactor, double yFactor) {
        List<Node> multiplied = new ArrayList<>();
        for (Node node : nodes) {
            Node mn = new Node();
            for (Vector v: node.getKeyPoints()) {
                mn.getKeyPoints().add(v.getMultiplied(xFactor, yFactor));
            }
            multiplied.add(mn);
        }
        return new Leg(multiplied);
    }

    @Override
    public String toString() {
        return nodes.toString();
    }


    public Leg node() {
        getNodes().add(new Node());
        return this;
    }

    public Leg kp(double x, double y) {
        getNodes().get(getNodes().size() - 1).getKeyPoints().add(new Vector(x, y));
        return this;
    }

    public Leg kp(Vector p) {
        getNodes().get(getNodes().size() - 1).getKeyPoints().add(new Vector(p.getX(), p.getY()));
        return this;
    }

    public Vector translatePoint(Vector point) {
        Vector v = new Vector(point);
        for (Node node: getNodes()) {
            Vector p = node.getPoint(position);
            v.transpose(p);
        }
        return v;
    }
}
