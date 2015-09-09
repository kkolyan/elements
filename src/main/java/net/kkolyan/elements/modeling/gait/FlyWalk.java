package net.kkolyan.elements.modeling.gait;

import net.kkolyan.elements.engine.core.Located;
import net.kkolyan.elements.engine.core.graphics.Curve;
import net.kkolyan.elements.engine.core.templates.DefaultCurve;
import net.kkolyan.elements.engine.core.templates.Vector;
import net.kkolyan.elements.engine.utils.ResourcesUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.floor;

/**
 * @author nplekhanov
 */
public class FlyWalk implements AdjustableClip {

    private Map<String, BufferedImage> images = new HashMap<>();

    private Vector cursor = new Vector();

    private double movePhase;
    private double attackPhase;
    private double scale = 1;

    private boolean await;
    private boolean attack;
    private boolean move;

    public FlyWalk await() {
        await = true;
        return this;
    }

    public FlyWalk attack() {
        attack = true;
        return this;
    }

    public FlyWalk move() {
        move = true;
        return this;
    }

    public static class SaveAll {
        public static void main(String[] args) {
            ClipSaver.save(new FlyWalk().await(), "game/fly/fly-high-await", 0.25);
            ClipSaver.save(new FlyWalk().attack(), "game/fly/fly-high-attack", 0.25);
            ClipSaver.save(new FlyWalk().move(), "game/fly/fly-low-move", 0.25);
        }
    }

    public static class RunAll {
        public static void main(String[] args) {
            ClipRunner.run(new FlyWalk().scale(0.25).attack(), Color.blue, 16);
        }
    }

    private FlyWalk scale(double scale) {
        setScale(scale);
        return this;
    }

    @Override
    public void moveCursor(int x, int y) {
        cursor.transpose(x, y);
        System.out.println("translated to " + cursor.getX() + ", " + cursor.getY() + "");
    }

    @Override
    public void init() throws Exception {
    }

    @Override
    public int getLength() {
        if (await) {
            return 1;
        }
        if (attack && !move) {
            return 16;
        }
        return 36;
    }

    @Override
    public void setScale(double scale) {
        this.scale = scale;
    }

    @Override
    public Dimension getSize() {
        Dimension size = new Dimension(1024, 1024);
        return new Dimension((int) (size.getWidth() * scale), (int) (size.getHeight() * scale));
    }

    @Override
    public void updateAndRender(Graphics2D canvas, int frameIndex) {

        canvas.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        canvas.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        if (move) {
            movePhase = frameIndex / (double) getLength();
        }
        if (attack) {
            attackPhase = frameIndex / (double) getLength();
        }

        Leg lf = new Leg()
                .node().kp(5, 10)
                .node().kp(0, 120).kp(50, 110).kp(120, 80)
                .node().kp(50, 5).kp(50, 10).kp(50, 40)
                .node().kp(10, 2)
                .getMultiplied(0.8)
                .setPosition(calcMovePhase(0.25));

        Leg rf = lf
                .getMultiplied(1, -1)
                .setPosition(calcMovePhase(0.75));

        Leg lb =  new Leg()
                .node().kp(5, 10)
                .node().kp(0, 120).kp(50, 110).kp(120, 80)
                .node().kp(50, 5).kp(50, 10).kp(50, 40)
                .node().kp(10, 5)
                .getMultiplied(-1, 1)
                .setPosition(-calcMovePhase(0.5));

        Leg rb = lb
                .getMultiplied(1, -1)
                .setPosition(-calcMovePhase(0));

        double ap = twoWay(attackPhase, 0.1);
        Vector headAttackMovement = new Vector((ap - 1) * 30, 0);
        Vector breastAttackMovement = new Vector((ap - 1) * 20, 0);

        Leg rHand = new Leg()
                .node().kp(new Vector(100, 16).getTranslated(headAttackMovement))
                .node().kp(-102, 34).kp(-42, 54)
                .node().kp(-47, 47).kp(23, 27)
                .node().kp(18, 6)
                .setPosition(ap);

        Leg lHand = new Leg()
                .node().kp(new Vector(100, -16).getTranslated(headAttackMovement))
                .node().kp(-15, 51).kp(45, 51)
                .node().kp(-58, 40).kp(12, 40)
                .node().kp(16, 16)
                .setPosition(ap);

        if (move) {

            paintLeg(canvas, lf);
            paintLeg(canvas, rf);
            paintLeg(canvas, lb);
            paintLeg(canvas, rb);

            drawImage(canvas, new Image("docs/art/fly/fly-belly.png").offset(-118, 0).scale(0.3).rotation((0.5 - twoWay(shift(movePhase, 0.2), 0.5)) * 5));
        }

        if (attack || await) {
            drawImage(canvas, new Image("docs/art/fly/fly-clip.png").offset(lHand.translatePoint(new Vector(111.0, -27.0))).scale(0.3));
            paintLeg(canvas, lHand);
            paintLeg(canvas, rHand);
            drawImage(canvas, new Image("docs/art/fly/fly-wings.png")
                    .offset(new Vector(-43, -7).getTranslated(breastAttackMovement))
                    .scale(0.3));
            drawImage(canvas, new Image("docs/art/fly/fly-breast.png")
                    .offset(new Vector(60, 3).getTranslated(breastAttackMovement))
                    .scale(new Vector((3 + ap) / 4, 1).getMultiplied(0.3)));

            drawImage(canvas, new Image("docs/art/fly/fly-head.png")
                    .offset(new Vector(175, -2).getTranslated(headAttackMovement))
                    .scale(0.3));
        }


    }

    private void drawImage(Graphics2D canvas, Image img) {
        String resource = img.getResource();
        BufferedImage image = images.get(resource);
        if (image == null) {
            image = readImage(resource);
            images.put(resource, image);
        }
        canvas.translate(getSize().getWidth()/2, getSize().getHeight()/2);

        canvas.rotate(img.getRotation() * Math.PI / 180);
        canvas.scale(this.scale, this.scale);
        canvas.translate(img.getOffset().getX(), img.getOffset().getY());

        canvas.scale(img.getScale().getX(), img.getScale().getY());
        canvas.translate(-image.getWidth() / 2, -image.getHeight() / 2);
        canvas.drawImage(image, 0, 0, null);
        canvas.setTransform(new AffineTransform());
    }

    private void paintLeg(Graphics2D canvas, Leg leg) {
        Curve curve = renderLeg(leg);
        canvas.setStroke(new BasicStroke(curve.getWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        Located last = curve.getPoints().get(0);
        for (int i = 1; i < curve.getPoints().size(); i ++) {
            Located p = curve.getPoints().get(i);
            canvas.translate(getSize().getWidth()/2, getSize().getHeight()/2);
            canvas.scale(scale, scale);
            canvas.setColor(Color.BLACK);
            canvas.drawLine((int)last.getX(), (int)last.getY(), (int)p.getX(), (int)p.getY());
            last = p;
            canvas.setTransform(new AffineTransform());
        }
    }

    private static double twoWay(double phase, double k) {
        double p = phase;
        if (p > k) {
            p = 1-(p - k) / (1 - k);
        } else {
            p = p / k;
        }
        p = 1-p*2;
        return p;
    }

    private double calcMovePhase(double shift) {

        double p = shift(movePhase, shift);
        return twoWay(p, 0.8);
    }

    private double shift(double phase, double shift) {
        double p = phase + shift;
        p = p - floor(p);
        return p;
    }

    private Curve renderLeg(Leg leg) {
        List<Located> points = new ArrayList<>();
        Vector v = new Vector();
        for (Node node : leg.getNodes()) {
            Vector p = node.getPoint(leg.getPosition());
            v = v.getTranslated(p);
            points.add(v);
        }
        return new DefaultCurve(points, 12);
    }

    private BufferedImage readImage(String resource) {
        try {
            byte[] bytes = ResourcesUtil.getResourceContent(resource);
            if (bytes == null) {
                return ImageIO.read(new File(resource));
            }
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
