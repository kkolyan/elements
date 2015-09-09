package net.kkolyan.elements.game;

import net.kkolyan.elements.engine.core.ControllerContext;
import net.kkolyan.elements.engine.core.Located;

/**
 * @author nplekhanov
 */
public class GtaStyleTankController extends CompoundUniObject implements GtaStyleControllable, TickListener {

    private Located target;

    private CombatUnit object;

    public void setObject(CombatUnit object) {
        this.object = object;
    }

    @Override
    public void afterTick(ControllerContext context) {
        object.aimTower(context.getTickLength(), target);
    }

    @Override
    public void beforeTick(ControllerContext context) {
    }

    @Override
    public void move(double timeDirection) {
        object.move(timeDirection);
    }

    @Override
    public void turn(double timeDirection) {
        object.turn(timeDirection);
    }

    @Override
    public void setTarget(Located target) {
        this.target = target;
    }

    @Override
    public Object shot() {
        return object.shot();
    }

    public Located getTarget() {
        return target;
    }

    @Override
    protected void initCompoundMultiObject() {
        addComponents(this, object);
    }
}
