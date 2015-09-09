package net.kkolyan.elements.game.tmx.model;

import net.kkolyan.elements.engine.core.templates.Vector;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.core.Commit;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author nplekhanov
 */
public class TmxPolyLine {

    @Attribute(name = "points") private String pointsRaw;
    @Transient private List<Vector> points;

    @Commit
    public void parsePoints() {
        boolean commasInsteadDotsAreLocal = Double.toString(12.543).equals("12,543");

        points = new ArrayList<>();
        for (String p: pointsRaw.split(" ")) {
            String[] c = p.split(",");
            Vector point = new Vector();
            if (commasInsteadDotsAreLocal) {
                c[0] = c[0].replace('.', ',');
                c[1] = c[1].replace('.', ',');
            }
            point.setX(Double.parseDouble(c[0]));
            point.setY(Double.parseDouble(c[1]));
            points.add(point);
        }
    }

    public String getPointsRaw() {
        return pointsRaw;
    }

    public void setPointsRaw(String pointsRaw) {
        this.pointsRaw = pointsRaw;
    }

    public List<Vector> getPoints() {
        return points;
    }
}
