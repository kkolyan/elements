package net.kkolyan.elements.engine.core.templates;

import net.kkolyan.elements.engine.core.Located;
import net.kkolyan.elements.engine.core.physics.Body;
import net.kkolyan.elements.engine.utils.Function;

/**
* @author nplekhanov
*/
public class BodyLocationFunction implements Function<Body, Located> {
    @Override
    public Located apply(Body body) {
        return body.getControllableObject();
    }
}
