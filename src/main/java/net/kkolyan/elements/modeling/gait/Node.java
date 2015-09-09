package net.kkolyan.elements.modeling.gait;

import net.kkolyan.elements.engine.core.templates.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nplekhanov
 */
public class Node {
    private List<Vector> keyPoints = new ArrayList<>();

    public Node() {
    }

    public Node(Vector center) {
        keyPoints.add(center);
    }

    public Node(Vector front, Vector center, Vector back) {
        keyPoints.add(back);
        keyPoints.add(center);
        keyPoints.add(front);
    }

    public List<Vector> getKeyPoints() {
        return keyPoints;
    }

    @Override
    public String toString() {
        return "{"+keyPoints+"}";
    }

    public Vector getPoint(double position) {

        double key = (position + 1) / 2 * (getKeyPoints().size() - 1);
        double floor = Math.floor(key);
        double ceil = Math.ceil(key);
        Vector a = getKeyPoints().get((int) floor);
        Vector b = getKeyPoints().get((int) ceil);
        Vector s = a.interpolateTo(b, key-floor);
        return new Vector(s.getX(), s.getY());
    }
}
