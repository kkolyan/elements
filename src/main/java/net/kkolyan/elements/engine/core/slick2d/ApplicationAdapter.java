package net.kkolyan.elements.engine.core.slick2d;

import net.kkolyan.elements.engine.core.*;
import net.kkolyan.elements.engine.core.slick2d.render.DrawableRendering;
import net.kkolyan.elements.engine.core.slick2d.render.LineRendering;
import net.kkolyan.elements.engine.core.slick2d.render.TextRendering;
import net.kkolyan.elements.engine.core.templates.Vector;
import net.kkolyan.elements.engine.utils.Function;
import net.kkolyan.elements.engine.utils.Profiling;
import net.kkolyan.elements.engine.utils.StopWatch;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;

import java.util.*;

/**
* @author nplekhanov
*/
public class ApplicationAdapter implements Game, SwitchableInputGame {
    private ResourceManager resourceManager = new ResourceManager();
    private Application application;

    private Set<String> persistentCommands = new LinkedHashSet<String>();
    private Set<String> transientCommands = new LinkedHashSet<>();
    private Map<String,int[]> measurableCommands = new LinkedHashMap<>();
    private Vector mouseViewportPosition = new Vector();

    private String typingBuffer = "";

    private InputAdapter inputAdapter = new InputAdapter(new InputHandlerImpl());
    private String title;
    private Shell shell;

    public ApplicationAdapter(Shell shell) {
        this.shell = shell;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @Override
    public void setAcceptingInput(boolean acceptingInput) {
        inputAdapter.setAcceptingInput(acceptingInput);
    }

    @Override
    public boolean closeRequested() {
        return true;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        container.getInput().addListener(inputAdapter);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        Debug.clear();

        StopWatch logicWatch = Profiling.startStopWatch("update");
        try {

            final Vector screenSize = new Vector(container.getWidth(), container.getHeight());

            application.handleTick(new ControllerContextImpl(Math.min(delta / 1000.0, 1), screenSize, container));
            transientCommands.clear();
            measurableCommands.clear();
            typingBuffer = "";
        } finally {
            logicWatch.stop();
        }

        if (cycleWatch != null) {
            cycleWatch.stop();
        }
        cycleWatch = Profiling.startStopWatch("cycle");
    }

    private StopWatch cycleWatch;


    private static Map<String,Color> colorsCache = new HashMap<String, Color>();

    private static Color getColor(String definition) {

        Color color = colorsCache.get(definition);
        if (color == null) {
            if (definition == null) {
                color = new Color(0, 0, 0, 0);
            } else {
                try {
                    color = (Color) Color.class.getField(definition).get(null);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                } catch (NoSuchFieldException e) {
                    throw new IllegalStateException("unknown color: "+definition,e);
                }

            }
            colorsCache.put(definition, color);
        }
        return color;
    }

    @Override
    public void render(GameContainer container, Graphics canvas) throws SlickException {

        StopWatch drawingWatch = Profiling.startStopWatch("render");
        try {

            canvas.setBackground(Color.black);
            canvas.setAntiAlias(true);

            Function<String, Color> colorFunction = new Function<String, Color>() {
                @Override
                public Color apply(String s) {
                    return getColor(s);
                }
            };

            DrawableRendering.drawDrawables(application, canvas, container, resourceManager);
            LineRendering.drawLines(application, canvas, container);
            TextRendering.drawTexts(application, canvas, container, colorFunction);
            RectangleRendering.drawRectangles(application, canvas, container, colorFunction);
            Debug.draw(application, container, canvas, colorFunction);
        } finally {
            drawingWatch.stop();
        }
    }

    public void executeCommand(String command) {
        transientCommands.add(command);

        if (command.startsWith("+")) {
            persistentCommands.add(command.substring(1));
        }
        if (command.startsWith("-")) {
            persistentCommands.remove(command.substring(1));
        }
    }

    public void bind(String inputCode, String command) {
        inputAdapter.bind(inputCode, command);
    }

    private class InputHandlerImpl implements InputHandler {

        @Override
        public void handleTyping(char letter) {
            typingBuffer += letter;
        }

        @Override
        public void handleCommand(String command) {
            shell.executeCommand(command);
        }

        @Override
        public void handleMousePosition(int x, int y) {
            mouseViewportPosition.set(x, y);
        }

        @Override
        public void handleMeasurableCommand(String command, int change) {
            int[] value = measurableCommands.get(command);
            if (value == null) {
                value = new int[] {0};
                measurableCommands.put(command, value);
            }
            value[0] += change;
        }
    }

    private class ControllerContextImpl implements ControllerContext {
        private final double tickSize;
        private final Vector screenSize;
        private GameContainer container;

        public ControllerContextImpl(double tickSize, Vector screenSize, GameContainer container) {
            this.tickSize = tickSize;
            this.screenSize = screenSize;
            this.container = container;
        }

        @Override
        public double getTickLength() {
            return tickSize;
        }

        @Override
        public boolean isCommandActive(String command) {
            return persistentCommands.contains(command) || transientCommands.contains(command);
        }

        @Override
        public int getMeasurableCommandValue(String command) {
            int[] value = measurableCommands.get(command);
            if (value == null) {
                return 0;
            }
            return value[0];
        }

        @Override
        public Vector getWorldMousePosition() {
            Vector screenWidth = new Vector(container.getWidth(), container.getHeight());
            Vector v = mouseViewportPosition.copy();
            v.transpose(screenWidth.getMultiplied(-0.5));// position related to screen center in screen units

            return new Vector(application.getViewPortCenter()).getTranslated(v.getMultiplied(application.getViewPortScale()));
        }

        @Override
        public Located getScreenSize() {
            return screenSize;
        }

        @Override
        public String getTypedCharacters() {
            return typingBuffer;
        }

        @Override
        public Set<String> getCommands() {
            return Collections.unmodifiableSet(transientCommands);
        }

        @Override
        public void setMouseCursor(String imageSetId) {
            try {
                SpriteSheet spriteSheet = resourceManager.getSpriteSheet(imageSetId);
                int[] tileParams = ResourceManager.getTileSizeAndOrigin(imageSetId);
                container.setMouseCursor(spriteSheet, tileParams[2], tileParams[3]);
            } catch (SlickException e) {
                throw new IllegalStateException(e);
            }
        }

    }

}
