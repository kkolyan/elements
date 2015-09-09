package net.kkolyan.elements.engine.core.definition.sdl;

import net.kkolyan.elements.engine.core.definition.StructuredDefinitionsFormat;
import net.kkolyan.elements.engine.core.definition.Node;
import net.kkolyan.elements.engine.utils.ResourcesUtil;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * @author nplekhanov
 */
public class SdlShellDemo {
    public static void main(String[] args) {
        SdlShell sdlShell = new SdlShell();
        List<Node> nodes = StructuredDefinitionsFormat.parseDefinitions(new ByteArrayInputStream(ResourcesUtil.getResourceContent("game/common.sdl")));
        sdlShell.loadNodes(nodes);
        demo(sdlShell.createObject(new Node(null, "Tile")));
        demo(sdlShell.createObject(new Node(null, "EmoticonNormal")));
        demo(sdlShell.createObject(new Node(null, "EmoticonLarge")));
        demo(sdlShell.createObject(new Node(null, "Sprite")));
        demo(sdlShell.createObject(new Node(null, "SolidSprite")));
    }

    private static void demo(Object o) {
        System.out.println(o);
    }
}
