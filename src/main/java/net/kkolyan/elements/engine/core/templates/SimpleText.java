package net.kkolyan.elements.engine.core.templates;

import net.kkolyan.elements.engine.core.graphics.Text;

import java.util.Arrays;
import java.util.List;

/**
 * @author nplekhanov
 */
public class SimpleText extends Object2d implements Text {
    private String text;
    private String color;

    public SimpleText(String text, String color, double x, double y) {
        this.text = text;
        this.color = color;
        set(x, y);
    }

    @Override
    public List<String> getLines() {
        return Arrays.asList(text.split("\\n"));
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "SimpleText{" +
                "text='" + text + '\'' +
                ", color='" + color + '\'' +
                "} " + super.toString();
    }
}
