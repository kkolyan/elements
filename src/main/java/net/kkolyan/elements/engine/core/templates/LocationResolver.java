package net.kkolyan.elements.engine.core.templates;

import net.kkolyan.elements.engine.core.Located;
import net.kkolyan.elements.engine.utils.Function;
import net.kkolyan.elements.game.UniObject;

/**
 * @author nplekhanov
 */
public class LocationResolver implements Function<UniObject,Located> {
    @Override
    public Located apply(UniObject uniObject) {
        return uniObject.as(Located.class);
    }
}
