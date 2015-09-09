package net.kkolyan.elements.game;

import net.kkolyan.elements.engine.core.Located;

/**
 * @author nplekhanov
 */
public interface DiabloStyleControllable extends UniObject {

    void setPointToMove(Located point);

    void setTargetToAttack(Located point);

    void setAttentionPoint(Located attentionPoint);

    Object execute(double tickLength);

    TargetType getExpectedTargetType();

    public enum TargetType {
        POINT, OBJECT
    }
}
