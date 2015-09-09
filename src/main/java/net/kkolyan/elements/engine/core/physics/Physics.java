package net.kkolyan.elements.engine.core.physics;

import net.kkolyan.elements.engine.core.Debug;
import net.kkolyan.elements.engine.core.grid.Field;
import net.kkolyan.elements.engine.core.grid.Grid;
import net.kkolyan.elements.engine.core.templates.BodyLocationFunction;
import net.kkolyan.elements.engine.core.templates.Vector;
import net.kkolyan.elements.engine.utils.Profiling;
import net.kkolyan.elements.engine.utils.StopWatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author nplekhanov
 */
public class Physics {

    private <T> Collection<T> shuffle(Collection<T> collection) {
        List list = (List) collection;
        Collections.shuffle(list);
        return collection;
    }

    public void tick(double tickSize, Collection<? extends Body> bodies, List<Border> borders) {
        Profiling.addNumber("physics.bodies", bodies.size());
        Profiling.addNumber("physics.borders", borders.size());
        StopWatch watch = Profiling.startStopWatch("update/physics");
        try {
            if (bodies.isEmpty()) {
                return;
            }

            StopWatch shuffleWatch = Profiling.startStopWatch("update/physics/grid/shuffle");
            try {
//                shuffle(bodies);
            } finally {
                shuffleWatch.stop();
            }

            for (Body body: bodies) {
                double angularVelocity = body.getAngularVelocity().getValue();
                body.getControllableObject().rotate(tickSize * angularVelocity);
                double caf = 100 * body.getConstantFrictionFactor() * tickSize;
                double qaf = 0.0003 * body.getAirFrictionFactor()
                        * angularVelocity * angularVelocity
                        *  body.getBoundingRadius()*  body.getBoundingRadius()
                        /   body.getMass();
                double f = caf + qaf;
                if (f > body.getAngularVelocity().abs()) {
                    body.getAngularVelocity().setValue(0);
                } else {
                    double aff = Math.copySign(f, angularVelocity);
                    body.getAngularVelocity().add(-aff);
                }

            }

            double maxBoundingRadius = 0;
            for (Body body: bodies) {
                double size = body.getBoundingRadius() * 2;
                Debug.drawEllipse(body.getControllableObject(),
                        new Vector(size, size),
                        "green");

                maxBoundingRadius = Math.max(maxBoundingRadius, body.getBoundingRadius());
            }
            if (maxBoundingRadius == 0) {
                throw new IllegalStateException();
            }
            Profiling.addNumber("physics.maxBoundingRadius", maxBoundingRadius);

            StopWatch gridWatch = Profiling.startStopWatch("update/physics/grid");
            Field<Body> grid;
            try {
                grid = new Grid<>(maxBoundingRadius, new BodyLocationFunction());
                for (Body body: bodies) {
                    grid.add(body);
                }
                grid.rebuild();
            } finally {
                gridWatch.stop();
            }

            Collection<Runnable> collisionReactions = new ArrayList<Runnable>();


            StopWatch collisionWatch = Profiling.startStopWatch("update/physics/collisions");
            try {
                for (Body a: bodies) {
                    double distance = maxBoundingRadius + a.getBoundingRadius();
                    Collection<? extends Body> neighbours = grid.lookup(a.getControllableObject(), new Vector(distance * 2, distance * 2));

                    Profiling.addNumber("physics.neighbours", neighbours.size());
                    for (Body b: neighbours) {
                        if (a == b) {
                            continue;
                        }

                        // about 5% boost

                        Vector vectorBetween = a.getControllableObject().vectorTo(b.getControllableObject());
                        if (Math.abs(vectorBetween.getX()) > a.getBoundingRadius() + b.getBoundingRadius()) {
                            continue;
                        }
                        if (Math.abs(vectorBetween.getY()) > a.getBoundingRadius() + b.getBoundingRadius()) {
                            continue;
                        }


                        if (a.getControllableObject().distanceTo(b.getControllableObject()) < a.getBoundingRadius() + b.getBoundingRadius()) {
                            Vector a2b = a.getControllableObject().vectorTo(b.getControllableObject());
                            if (a2b.magnitude() == 0) {
                                continue;
                            }
                            a2b.multiply(1 / a2b.magnitude());

                            double va = a.getVelocity().dotProduct(a2b) / a2b.magnitude();
                            double vb = b.getVelocity().dotProduct(a2b) / a2b.magnitude();
                            double v = (a.getMass() * va + b.getMass() * vb) / (a.getMass() + b.getMass());

                            Vector aVelDelta = a2b.getMultiplied((v - va) / 2);
                            Vector bVelDelta = a2b.getMultiplied((v - vb) / 2);

                            if (va - vb > 0) {
                                collisionReactions.add(new VelocityChange(a, aVelDelta));
                                collisionReactions.add(new VelocityChange(b, bVelDelta));
                            }
                        }
                    }
                }

                for (Runnable task: collisionReactions) {
                    task.run();
                }
            } finally {
                collisionWatch.stop();
            }

            StopWatch frictionWatch = Profiling.startStopWatch("update/physics/friction");
            try {
                for (Body body: bodies) {
                    double v;

                    v = body.getVelocity().magnitude();
                    if (v > 0.0) {
                        Vector movementReactionUnit = body.getVelocity().getMultiplied(-1 / v);

                        double dragForce = body.getBoundingRadius() * body.getBoundingRadius() * body.getAirFrictionFactor() * v * v;
                        double constantFrictionForce = body.getConstantFrictionFactor() * body.getMass();
                        Vector reactionVelocityDelta = movementReactionUnit.getMultiplied(
                                (dragForce + constantFrictionForce) * tickSize / body.getMass());

                        if (reactionVelocityDelta.magnitude() < v) {
                            body.getVelocity().transpose(reactionVelocityDelta);
                        } else {
                            body.getVelocity().multiply(0);
                        }
                    }
                    v = body.getVelocity().magnitude();
                    if (v > 0.0) {
                        if (body.getLateralFrictionFactor() > 0) {
                            Vector lateral = Vector.fromAngle(body.getControllableObject().getDirection() + 90, 1);
                            Vector lateralVelocity = lateral.getProjectionOf(body.getVelocity());

                            double lateralVelocityMagnitude = lateralVelocity.magnitude();
                            if (lateralVelocityMagnitude > 0) {
                                double frictionMagnitude = Math.min(lateralVelocityMagnitude, body.getLateralFrictionFactor());
                                Vector lateralReaction = lateralVelocity.getMultiplied(-frictionMagnitude / lateralVelocityMagnitude);

                                body.getVelocity().transpose(lateralReaction);
                            }
                        }
                    }
                }
            } finally {
                frictionWatch.stop();
            }

            StopWatch bordersWatch = Profiling.startStopWatch("update/physics/borders");
            try {
                Collection<Runnable> borderReactions = new ArrayList<Runnable>();
                for (Border border: borders) {
                    Debug.drawLine(border.getBegin(), border.getEnd(), "white");
                    Collection<? extends Body> borderBodies = grid.lookupAtLongArea(border.getBegin(), border.getEnd(), maxBoundingRadius);
                    Profiling.addNumber("physics.bodiesPerBorder", borderBodies.size());
                    for (Body body: borderBodies) {
                        Vector borderVector = border.getBegin().vectorTo(border.getEnd());

                        // arm - segment between point and begin of border
                        Vector arm = border.getBegin().vectorTo(body.getControllableObject());
                        // leg - segment between begin of border and normal from border to body
                        double legLength = arm.magnitude() * borderVector.cos(arm);

                        Vector normalPoint;

                        if (legLength < 0) {
                            normalPoint = border.getBegin();
                        } else if (legLength > borderVector.magnitude()) {
                            normalPoint = border.getEnd();
                        } else {
                            normalPoint = border.getBegin().getTranslated(borderVector.getNormalized().getMultiplied(legLength));
                        }

                        Vector altitude = normalPoint.vectorTo(body.getControllableObject());

                        if (altitude.magnitude() > body.getBoundingRadius()) {
                            continue;
                        }


                        double v = body.getVelocity().dotProduct(altitude) / altitude.magnitude();

                        if (v < 0) {
                            Vector change = altitude.getNormalized().getMultiplied(-v);
        //                    borderReactions.add(new VelocityChange(body, change));
                            body.getVelocity().transpose(change);
                        }
                    }
                }

                for (Runnable task: borderReactions) {
                    task.run();
                }
            } finally {
                bordersWatch.stop();
            }

            for (Body body: bodies) {
                body.getControllableObject().transpose(body.getVelocity());
            }
        } finally {
            watch.stop();
        }
    }

    private static class VelocityChange implements Runnable {
        private Body body;
        private Vector delta;

        public VelocityChange(Body body, Vector delta) {
            this.body = body;
            this.delta = delta;
        }

        @Override
        public void run() {
            body.getVelocity().transpose(delta);
        }
    }

}
