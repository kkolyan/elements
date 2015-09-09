package net.kkolyan.elements.engine.core.definition;

import net.kkolyan.elements.engine.utils.ResourcesUtil;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author nplekhanov
 */
public class StructuredDefinitionsFormatTest {

    @Test
    public void testParse1() throws IOException {
        List<Node> nodes = parse("definition1.txt");
        assertEquals(1, nodes.size());
        assertEquals(null, nodes.get(0).getName());
        assertEquals("A great root", nodes.get(0).getValue());

        assertEquals(5, nodes.get(0).getChildren().size());
        assertEquals("x", nodes.get(0).getChildren().get(0).getName());
        assertEquals("5", nodes.get(0).getChildren().get(0).getValue());

        assertEquals("y", nodes.get(0).getChildren().get(1).getName());
        assertEquals("6", nodes.get(0).getChildren().get(1).getValue());

        assertEquals("parent", nodes.get(0).getChildren().get(2).getName());
        assertEquals("B", nodes.get(0).getChildren().get(2).getValue());
        assertEquals(2, nodes.get(0).getChildren().get(2).getChildren().size());
        assertEquals("x", nodes.get(0).getChildren().get(2).getChildren().get(0).getName());
        assertEquals("12", nodes.get(0).getChildren().get(2).getChildren().get(0).getValue());
        assertEquals("y", nodes.get(0).getChildren().get(2).getChildren().get(1).getName());
        assertEquals("17", nodes.get(0).getChildren().get(2).getChildren().get(1).getValue());


        assertEquals("brother", nodes.get(0).getChildren().get(3).getName());
        assertEquals("C", nodes.get(0).getChildren().get(3).getValue());
        assertEquals(2, nodes.get(0).getChildren().get(3).getChildren().size());
        assertEquals("x", nodes.get(0).getChildren().get(3).getChildren().get(0).getName());
        assertEquals("-7", nodes.get(0).getChildren().get(3).getChildren().get(0).getValue());
        assertEquals("y", nodes.get(0).getChildren().get(3).getChildren().get(1).getName());
        assertEquals("56", nodes.get(0).getChildren().get(3).getChildren().get(1).getValue());

        assertEquals("name", nodes.get(0).getChildren().get(4).getName());
        assertEquals("Car", nodes.get(0).getChildren().get(4).getValue());


    }

    @Test
    public void testParse2() throws IOException {
        List<Node> nodes = parse("definition2.txt");

        assertEquals(5, nodes.size());
        assertEquals("x", nodes.get(0).getName());
        assertEquals("5", nodes.get(0).getValue());

        assertEquals("y", nodes.get(1).getName());
        assertEquals("6", nodes.get(1).getValue());

        assertEquals("parent", nodes.get(2).getName());
        assertEquals("B", nodes.get(2).getValue());
        assertEquals(2, nodes.get(2).getChildren().size());
        assertEquals("x", nodes.get(2).getChildren().get(0).getName());
        assertEquals("12", nodes.get(2).getChildren().get(0).getValue());
        assertEquals("y", nodes.get(2).getChildren().get(1).getName());
        assertEquals("17", nodes.get(2).getChildren().get(1).getValue());


        assertEquals("brother", nodes.get(3).getName());
        assertEquals("C", nodes.get(3).getValue());
        assertEquals(2, nodes.get(3).getChildren().size());
        assertEquals("x", nodes.get(3).getChildren().get(0).getName());
        assertEquals("-7", nodes.get(3).getChildren().get(0).getValue());
        assertEquals("y", nodes.get(3).getChildren().get(1).getName());
        assertEquals("56", nodes.get(3).getChildren().get(1).getValue());

        assertEquals("name", nodes.get(4).getName());
        assertEquals("Car", nodes.get(4).getValue());


    }

    @Test
    public void testFormat1() throws IOException {
        List<Node> nodes = parse("definition1.txt");
        ByteArrayOutputStream formatted = new ByteArrayOutputStream();
        StructuredDefinitionsFormat.formatDefinitions(nodes, formatted);

        assertEquals(new String(ResourcesUtil.getResourceContent("definition1.txt")), formatted.toString());
    }

    @Test
    public void testFormat2() throws IOException {
        List<Node> nodes = parse("definition2.txt");
        ByteArrayOutputStream formatted = new ByteArrayOutputStream();
        StructuredDefinitionsFormat.formatDefinitions(nodes, formatted);

        assertEquals(new String(ResourcesUtil.getResourceContent("definition2.txt")), formatted.toString());
    }

    private List<Node> parse(String resource) throws IOException {
        return StructuredDefinitionsFormat.parseDefinitions(new ByteArrayInputStream(ResourcesUtil.getResourceContent(resource)));
    }


}
