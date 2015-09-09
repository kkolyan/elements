package net.kkolyan.elements.game;

import net.kkolyan.elements.engine.core.ControllerContext;
import net.kkolyan.elements.engine.core.Locatable;
import net.kkolyan.elements.engine.core.Located;
import net.kkolyan.elements.engine.core.templates.SolidSprite;

import java.util.Collection;
import java.util.Collections;

/**
 * @author nplekhanov
 */
public class NPC extends CompoundUniObject implements TickListener, PlayerListener {
    private Object controllable;
    private Object player;
    private double hearRadius;

    public void setHearRadius(double hearRadius) {
        this.hearRadius = hearRadius;
    }

    public void setControllable(Object controllable) {
        this.controllable = controllable;
    }

    @Override
    public void beforeTick(ControllerContext context) {

    }

    @Override
    public void afterTick(ControllerContext context) {

    }

    @Override
    public void setPlayer(Object player) {
        this.player = player;
    }

    @Override
    protected void initCompoundMultiObject() {
        addComponents(this, controllable);
    }
}
