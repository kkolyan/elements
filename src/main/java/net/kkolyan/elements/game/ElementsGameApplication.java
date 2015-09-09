package net.kkolyan.elements.game;

import net.kkolyan.elements.engine.core.Locatable;
import net.kkolyan.elements.engine.core.Located;
import net.kkolyan.elements.engine.core.Shot;
import net.kkolyan.elements.engine.core.definition.sdl.SdlLoader;
import net.kkolyan.elements.engine.core.grid.Field;
import net.kkolyan.elements.engine.core.grid.Grid;
import net.kkolyan.elements.engine.core.templates.*;
import net.kkolyan.elements.engine.core.templates.BaseApplication;
import net.kkolyan.elements.engine.core.ControllerContext;
import net.kkolyan.elements.engine.core.graphics.Curve;
import net.kkolyan.elements.engine.core.graphics.Text;
import net.kkolyan.elements.engine.core.physics.Body;
import net.kkolyan.elements.engine.core.physics.Border;
import net.kkolyan.elements.engine.core.physics.Physics;
import net.kkolyan.elements.engine.core.graphics.Drawable;
import net.kkolyan.elements.engine.core.templates.Vector;
import net.kkolyan.elements.engine.utils.Function;
import net.kkolyan.elements.engine.utils.ObjectProvider;
import net.kkolyan.elements.engine.utils.StringUtil;
import net.kkolyan.elements.game.tmx.TmxLoader;

import java.util.*;

/**
 * @author nplekhanov
 */
public class ElementsGameApplication extends BaseApplication {
    private Field<UniObject> spritesField = new Grid<>(64, new LocationResolver());
    private Vector viewPortCenter = new Vector();
    private Collection<Text> texts = new ArrayList<>();
    private Physics physics = new Physics();

    private List<Drawable> visibleSprites = new ArrayList<>();

    private List<UniObject> players;

    private Collection<TickListener> tickListeners = new ArrayList<>();
    private Collection<PlayerListener> playerListeners = new ArrayList<>();
    private Field<UniObject> bodiesField = new Grid<>(64, new LocationResolver());
    private List<Border> borders = new ArrayList<>();
    private SdlLoader sdlLoader = new SdlLoader();

    private int powerOfScale;
    private boolean showCoordinates;
    private boolean showBorders;

    public void setPlayers(List<UniObject> players) {
        this.players = players;
    }

    public void setComponents(Collection<?> components) {
        for (Object component: components) {
            addComponent(component);
        }
    }

    private void addComponent(Object component) {
        int matches = 0;

        if (component instanceof UniObject) {
            UniObject o = (UniObject) component;

            if (o.is(Drawable.class)) {
                spritesField.add(o);
                matches ++;
            }
            if (o.is(Body.class)) {
                bodiesField.add(o);
                matches ++;
            }
            if (tickListeners.addAll(o.of(TickListener.class))) {
                matches ++;
            }
            if (o.is(Text.class)) {
                getScreenText().add(o.as(Text.class));
                matches ++;
            }
            if (o.is(Border.class)) {
                borders.add(o.as(Border.class));
                matches ++;
            }
            if (o.is(ObjectProvider.class)) {
                Object object = (o.as(ObjectProvider.class)).getObject();
                addComponent(object);
                matches ++;
            }
            if (o.is(PlayerListener.class)) {
                playerListeners.add(o.as(PlayerListener.class));
            }
        } else {
            addComponent(new SimpleUniObject(component));
            matches ++;
        }
        if (component instanceof Collection) {
            for (Object element: (Collection)component) {
                addComponent(element);
            }
            matches ++;
        }
        if (matches == 0) {
            throw new IllegalStateException("component "+component+" is not recognized as supported component");
        }
    }

    @Override
    public void handleTick(ControllerContext context) {

        if (context.isCommandActive("+toggle_player")) {
            UniObject o = players.remove(0);
            players.add(o);
            for (PlayerListener pl: playerListeners) {
                pl.setPlayer(o);
            }
        }

        for (TickListener tickListener: tickListeners) {
            tickListener.beforeTick(context);
        }

        final UniObject player = getPlayer();

        if (player != null) {

            if (player.is(CrimsonLandStyleTankController.class)) {
                CrimsonLandStyleControllable c = player.as(CrimsonLandStyleControllable.class);

                Vector movement = new Vector();

                if (context.isCommandActive("crimson.up")) {
                    movement.transpose(0, -1);
                }
                if (context.isCommandActive("crimson.down")) {
                    movement.transpose(0, 1);
                }
                if (context.isCommandActive("crimson.left")) {
                    movement.transpose(-1, 0);
                }
                if (context.isCommandActive("crimson.right")) {
                    movement.transpose(1, 0);
                }
                if (movement.magnitude() > 0.0) {
                    movement = movement.getNormalized();
                    c.move(context.getTickLength(), movement.angle());
                }

                if (context.isCommandActive("crimson.shot")) {
                    Object effect = c.shot();
                    if (effect instanceof Shot) {
                        ((Shot) effect).invoke(spritesField);
                    }
                }

                c.setTarget(context.getWorldMousePosition());

            }

            if (player.is(GtaStyleControllable.class)) {
                GtaStyleControllable c = player.as(GtaStyleControllable.class);

                if (context.isCommandActive("gta.forward")) {
                    c.move(context.getTickLength());
                }

                if (context.isCommandActive("gta.backward")) {
                    c.move(-context.getTickLength());
                }

                if (context.isCommandActive("gta.left")) {
                    c.turn(context.getTickLength());
                }

                if (context.isCommandActive("gta.right")) {
                    c.turn(-context.getTickLength());
                }

                if (context.isCommandActive("gta.shot")) {
                    Object effect = c.shot();
                    if (effect instanceof Shot) {
                        ((Shot) effect).invoke(spritesField);
                    }
                }

                c.setTarget(context.getWorldMousePosition());
            }

            if (player.is(DiabloStyleControllable.class)) {
                DiabloStyleControllable c = player.as(DiabloStyleControllable.class);

                c.setAttentionPoint(context.getWorldMousePosition());

                if (context.isCommandActive("diablo.attack")) {

                    if (c.getExpectedTargetType() == DiabloStyleControllable.TargetType.OBJECT) {
                        List<UniObject> candidates = spritesField.lookup(context.getWorldMousePosition(), new Vector());

                        if (!candidates.isEmpty()) {
                            final Map<UniObject,Double> distances = new IdentityHashMap<>();
                            for (UniObject candidate: candidates) {
                                distances.put(candidate, context.getWorldMousePosition().distanceTo(candidate.as(Located.class)));
                            }
                            UniObject target = Collections.min(candidates, new Comparator<UniObject>() {
                                @Override
                                public int compare(UniObject o1, UniObject o2) {
                                    return Double.compare(distances.get(o1), distances.get(o2));
                                }
                            });

                            c.setTargetToAttack(target.as(Located.class));
                        }
                    }

                    if (c.getExpectedTargetType() == DiabloStyleControllable.TargetType.POINT) {
                        c.setTargetToAttack(context.getWorldMousePosition());
                    }

                }

                if (context.isCommandActive("diablo.move")) {
                    c.setPointToMove(context.getWorldMousePosition());
                }

                Object effect = c.execute(context.getTickLength());
                if (effect instanceof Shot) {
                    ((Shot) effect).invoke(spritesField);
                }
            }
        }

        if (context.isCommandActive("showCoordinates")) {
            showCoordinates = !showCoordinates;
        }

        if (context.isCommandActive("showBorders")) {
            showBorders = !showBorders;
        }

        powerOfScale -= context.getMeasurableCommandValue("zoom");

        Status.addLine("zoom: "+powerOfScale);

        for (String command: context.getCommands()) {
            if (command.startsWith("level ")) {
                spritesField.clear();
                bodiesField.clear();

                String argument = StringUtil.argument(command);

                Level level = TmxLoader.loadTmxLevel(argument, sdlLoader);

                for (Object o: level.getObjects()) {
                    addComponent(o);
                }
                for (UniObject p: players) {
                    if (p.is(Locatable.class)) {
                        p.as(Locatable.class).set(level.getStartPosition());
                    }
                    addComponent(p);
                }
            }
            if (command.startsWith("load ")) {
                sdlLoader.loadSdl(StringUtil.argument(command));
            }
            if (command.startsWith("player ")) {
                Object p = sdlLoader.createObject(StringUtil.argument(command));
                players = new ArrayList<>();
                if (p instanceof Collection) {
                    for (Object sub: (Collection)p) {
                        if (sub instanceof UniObject) {
                            players.add((UniObject) sub);
                        }
                    }
                } else {
                    players.add((UniObject) p);
                }
            }
            if (command.startsWith("addComponent ")) {
                Object component = sdlLoader.createObject(StringUtil.argument(command));
                addComponent(component);
            }
            if (command.startsWith("cursor ")) {
                context.setMouseCursor(StringUtil.argument(command));
            }
        }


        Collection<UniObject> bodies = this.bodiesField.lookup(getViewPortCenter(), new Vector(context.getScreenSize()).getMultiplied(2 * getViewPortScale()));
        Status.addLine("Physic bodies: "+bodies.size());
        physics.tick(context.getTickLength(), UniObjects.projectRequiredComponents(bodies, Body.class), borders);


        for (TickListener tickListener: tickListeners) {
            tickListener.afterTick(context);
        }

        for (UniObject body: bodies) {
            this.bodiesField.update(body);
        }

        for (UniObject body: bodies) {
            spritesField.update(body);
        }

        if (player != null) {
            if (player.is(Located.class)) {
                getViewPortCenter().set(player.as(Located.class));
                Status.addLine("Position: " + new Vector(player.as(Located.class)));
            }
            if (player.is(SolidSprite.class)) {
                Status.addLine("Velocity: " + player.as(SolidSprite.class).getVelocity());
            }
        }

        List<UniObject> visibleUniObjects = spritesField.lookup(getViewPortCenter(), new Vector(context.getScreenSize()).getMultiplied(1.2 * getViewPortScale()));
        visibleSprites = UniObjects.extractComponents(visibleUniObjects, Drawable.class, new ArrayList<Drawable>());
        Status.addLine("Visible bodies: "+visibleSprites.size());

        Status.addLine("Total sprites: "+spritesField.getObjectsCount());

        Status.addLine("scale: "+getViewPortScale());
    }

    private UniObject getPlayer() {
        if (players == null || players.isEmpty()) {
            return null;
        }
        return players.get(0);
    }

    @Override
    public Collection<Text> getScreenText() {
        return texts;
    }

    @Override
    public List<Drawable> getWorldSprites() {
        return visibleSprites;
    }

    @Override
    public Collection<? extends Text> getWorldText() {
        if (!showCoordinates) {
            return super.getWorldText();
        }
        Collection<Text> worldTexts = new ArrayList<>();
        for (Drawable drawable: visibleSprites) {
            if (drawable instanceof Body) {
                worldTexts.add(new SimpleText(new Vector(drawable).toString(),"red", drawable.getX(), drawable.getY()));
            }
        }
        return worldTexts;
    }

    @Override
    public Vector getViewPortCenter() {
        return viewPortCenter;
    }

    @Override
    public List<? extends Curve> getWorldCurves() {
        List<Curve> curves = new ArrayList<Curve>();
        if (showBorders) {
            for (Border border: borders) {
                curves.add(new DefaultCurve(Arrays.asList(border.getBegin(), border.getEnd())));
            }
        }
        return curves;
    }

    @Override
    public double getViewPortScale() {
        return Math.pow(1.1, powerOfScale / 120.0);
    }
}
