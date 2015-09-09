package net.kkolyan.elements.game.tmx.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.core.Commit;

import java.util.Map;

/**
 * @author nplekhanov
 */
public class TmxData {
    @Attribute private String encoding;
    @Text private String content;
    @Transient private int[][] gidMatrix;

    @Commit
    public void parseGidMatrix() {
        String[] lines = content.trim().split("\n");
        gidMatrix = new int[lines.length][];
        for (int row = 0; row < lines.length; row ++) {
            String line = lines[row].trim();
            if (line.endsWith(",")) {
                line = line.substring(0, line.length() - 1);
            }
            String[] split = line.split(",");
            gidMatrix[row] = new int[split.length];
            for (int column = 0; column < split.length; column++) {
                String cell = split[column].trim();
                gidMatrix[row][column] = Integer.parseInt(cell);
            }
        }
    }

    public int[][] getGidMatrix() {
        return gidMatrix;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
