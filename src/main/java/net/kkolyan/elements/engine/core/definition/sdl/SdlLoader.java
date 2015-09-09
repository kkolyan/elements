package net.kkolyan.elements.engine.core.definition.sdl;

import net.kkolyan.elements.engine.core.definition.StructuredDefinitionsFormat;
import net.kkolyan.elements.engine.core.definition.Node;
import net.kkolyan.elements.engine.utils.ResourcesUtil;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.List;

/**
 * @author nplekhanov
 */
public class SdlLoader {
    private SdlShell sdlShell = new SdlShell();

    public Collection<String> getObjectNames() {
        return sdlShell.getObjectNames();
    }

    public void loadSdl(String resource) {
        List<Node> nodes = loadNodes(resource);
        sdlShell.loadNodes(nodes);
    }

    public Object createObject(String name) {
        return sdlShell.createObject(name);
    }

    public static List<Node> loadNodes(String resource) {
        return StructuredDefinitionsFormat.parseDefinitions(new ByteArrayInputStream(
                ResourcesUtil.getResourceContent(resource)));
    }

}
