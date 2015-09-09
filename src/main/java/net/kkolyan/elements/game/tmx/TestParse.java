package net.kkolyan.elements.game.tmx;

import net.kkolyan.elements.engine.utils.ResourcesUtil;
import net.kkolyan.elements.game.tmx.model.TmxMap;
import org.simpleframework.xml.core.Persister;

import java.io.ByteArrayInputStream;

/**
 * @author nplekhanov
 */
public class TestParse {
    public static void main(String[] args) throws Exception {
        Persister persister = new Persister();
        TmxMap tmxMap = persister.read(TmxMap.class, new ByteArrayInputStream(ResourcesUtil.getResourceContent("game/level1.tmx")));
        System.out.println(tmxMap);
    }
}
