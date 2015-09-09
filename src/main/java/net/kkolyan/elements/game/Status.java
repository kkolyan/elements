package net.kkolyan.elements.game;

import net.kkolyan.elements.engine.core.ControllerContext;
import net.kkolyan.elements.engine.core.templates.Object2d;
import net.kkolyan.elements.engine.core.graphics.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nplekhanov
 */
public class Status extends Object2d implements Text, TickListener {
    private static List<String> lines = new ArrayList<String>();

    public static void addLine(String line) {
        for (String s: line.split("\n")) {
            if (!s.trim().isEmpty()) {
                lines.add(line);
            }
        }
    }

    @Override
    public List<String> getLines() {
        return lines;
    }

    @Override
    public String getColor() {
        return "white";
    }

    @Override
    public void beforeTick(ControllerContext context) {
        lines.clear();
    }

    @Override
    public void afterTick(ControllerContext context) {

    }
}
