package net.kkolyan.elements.engine.core;

import net.kkolyan.elements.engine.core.graphics.Drawable;
import net.kkolyan.elements.engine.core.grid.Field;
import net.kkolyan.elements.engine.core.physics.Body;
import net.kkolyan.elements.engine.core.templates.SolidSprite;
import net.kkolyan.elements.engine.core.templates.Sprite;
import net.kkolyan.elements.engine.core.templates.Vector;
import net.kkolyan.elements.engine.utils.CollectionUtil;
import net.kkolyan.elements.game.UniObject;
import net.kkolyan.elements.game.UniObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author nplekhanov
 */
public class Shot {
    private Located actor;
    private double direction;
    private double power;

    public Shot(Located actor, double direction, double power) {
        this.actor = actor;
        this.direction = direction;
        this.power = power;
    }

    public void invoke(Field<UniObject> spritesField) {
        Vector trajectory = Vector.fromAngle(direction, 1024);
        List<UniObject> preCandidates = spritesField.lookupAtLongArea(actor, trajectory.getTranslated(actor), 32);
        List<Body> candidates = UniObjects.extractComponents(preCandidates, Body.class, new ArrayList<Body>());

        Debug.drawLine(actor, new Vector(actor).getTranslated(trajectory), "blue");

        Collections.sort(candidates, new Comparator<Body>() {
            @Override
            public int compare(Body o1, Body o2) {
                return Double.compare(distance(o1), distance(o2));
            }

            private double distance(Body o) {
                return new Vector(actor).distanceTo(o.getControllableObject());
            }
        });

        Body victim = null;
        for (Body candidate: candidates) {
            if (candidate == actor) {
                continue;
            }
            Vector perfectTrajectory = new Vector(actor).vectorTo(candidate.getControllableObject());
            if (perfectTrajectory.dotProduct(trajectory) < 0) {
                continue;
            }
            Vector a = trajectory.getProjectionOf(perfectTrajectory);
            double distance = a.getTranslated(actor).distanceTo(candidate.getControllableObject());
            if (distance < candidate.getBoundingRadius()) {
                victim = candidate;
                break;
            }
        }
        if (victim != null) {
            Vector v = trajectory.getMultiplied(power / trajectory.magnitude() / victim.getMass());
            victim.getVelocity().transpose(v);
        }

        if (actor instanceof SolidSprite) {
            SolidSprite solid = (SolidSprite) actor;
            solid.getVelocity().transpose(trajectory.getMultiplied(- power / trajectory.magnitude() / solid.getMass()));
        }
    }
}
