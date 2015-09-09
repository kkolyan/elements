package net.kkolyan.elements.game;

import net.kkolyan.elements.engine.core.Debug;
import net.kkolyan.elements.engine.core.Located;
import net.kkolyan.elements.engine.core.templates.Vector;

/**
 * @author nplekhanov
 */
public class DiabloStypeTankController extends CompoundUniObject implements DiabloStyleControllable {

    private Located pointToMove;
    private Located targetToAttack;
    private Located attentionPoint;

    private boolean bodyAimed;

    private CombatUnit object;

    public void setObject(CombatUnit object) {
        this.object = object;
    }

    @Override
    public void setPointToMove(Located pointToMove) {
        this.pointToMove = pointToMove;
    }

    @Override
    public void setTargetToAttack(Located pointToAttack) {
        this.targetToAttack = pointToAttack;
    }

    @Override
    public void setAttentionPoint(Located attentionPoint) {
        this.attentionPoint = attentionPoint;
    }

    @Override
    public Object execute(double tickLength) {
        Object effect = null;

        bodyAimed = false;

        if (targetToAttack != null) {
            Debug.drawEllipse(targetToAttack, new Vector(16, 16), "yellow");

            if (object.getTowerRotationLimit() < 360) {
                pointToMove = null;
            }

            aimBodyIfNecessary(tickLength, targetToAttack);
            object.aimTower(tickLength, targetToAttack);

            Vector attackVector = object.vectorTo(targetToAttack);
            double angleToTarget = object.normalizeAngle(attackVector.angle() - object.getTowerDirection());

            Status.addLine("angleToTarget: "+angleToTarget);
            if (Math.abs(angleToTarget) <= 1) {
                targetToAttack = null;
                effect = object.shot();
            }
        }

        if (pointToMove != null) {
            Debug.drawEllipse(pointToMove, new Vector(16, 16), "blue");
            Vector moveVector = object.vectorTo(pointToMove);
            if (moveVector.magnitude() < object.getBoundingRadius()/2) {
                pointToMove = null;
            } else {
                double angleToTarget = object.normalizeAngle(moveVector.angle() - object.getDirection());
                aimBody(tickLength, pointToMove);
                if (Math.abs(angleToTarget) < 45) {
                    object.move(tickLength);
                }
            }
        }

        if (attentionPoint != null) {
            aimBodyIfNecessary(tickLength, attentionPoint);
            object.aimTower(tickLength, attentionPoint);
        }

        return effect;
    }

    @Override
    public TargetType getExpectedTargetType() {
        return TargetType.POINT;
    }

    private void aimBodyIfNecessary(double tickLength, Located point) {
        if (point != null && object.getTowerRotationLimit() < 360) {
            aimBody(tickLength, point);
        }
    }

    private void aimBody(double tickLength, Located point) {
        if (bodyAimed) {
            return;
        }
        bodyAimed = true;
        Vector vector = object.vectorTo(point);
        double angleToTarget = object.normalizeAngle(vector.angle() - object.getDirection());

        if (Math.abs(angleToTarget) < 0.1 && object.getAngularVelocity().abs() < 10000000) {
            return;
        }
        double timeToReach = Math.abs(angleToTarget) / object.getAngularVelocity().abs();
        double timeToStop = object.getTimeToStop();
        double k;
        Status.addLine("angleToTarget: "+angleToTarget);
        Status.addLine("timeToReach: "+timeToReach);
        Status.addLine("timeToStop: "+timeToStop);
        if (timeToReach < timeToStop) {
            k = -Math.min(1, timeToStop/tickLength);
        } else {
            k = Math.min(1, timeToReach/tickLength);
        }
        Status.addLine("k: "+k);
        object.turn(k * -Math.copySign(tickLength, angleToTarget));
    }

    @Override
    protected void initCompoundMultiObject() {
        addComponents(this, object);
    }
}
