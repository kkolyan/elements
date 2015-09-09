package net.kkolyan.elements.engine.core.definition.sdl;

/**
 * @author nplekhanov
 */
public interface Constructor {
    Object create(ConstructionContext callerContext);
}
