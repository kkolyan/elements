package net.kkolyan.elements.engine.core.slick2d;

import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.awt.event.KeyEvent;
import java.util.*;

/**
 * Converts input signals to commands
 *
 * @author nplekhanov
 */
public class InputAdapter implements InputListener {

    private MultiValueMap<Integer,String> keyCodeToCommands = new LinkedMultiValueMap<>();
    private MultiValueMap<Integer,String> mouseButtonToCommands = new LinkedMultiValueMap<>();
    private Collection<String> mouseWheelCommands = new ArrayList<>();

    private InputHandler inputHandler;
    private boolean acceptingInput = true;

    public InputAdapter(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    public void bind(String inputCode, String command) {
        List<String> mouseButtons = Arrays.asList("","MOUSE1","MOUSE2","MOUSE3");

        if (inputCode.equals("MOUSE_WHEEL")) {
            mouseWheelCommands.add(command);
        } else if (mouseButtons.contains(inputCode)) {
            mouseButtonToCommands.add(mouseButtons.indexOf(inputCode), command);
        } else {
            keyCodeToCommands.add(resolveKeyCode(inputCode), command);
        }
    }

    private int resolveKeyCode(String inputCode) {
        Class c;
        if (inputCode.startsWith("KEY_")) {
            c = Input.class;
        } else {
            c = KeyEvent.class;
        }
        try {
            return (Integer) c.getField(inputCode).get(null);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("unknown input code: '"+inputCode+"'",e);
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        List<String> commands = keyCodeToCommands.get(key);
        if (commands != null) {
            for (String command: commands){
                inputHandler.handleCommand("+" + command);
            }
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        List<String> commands = keyCodeToCommands.get(key);
        if (commands != null) {
            for (String command: commands){
                inputHandler.handleCommand("-" + command);
            }
        }
    }

    @Override
    public void mouseWheelMoved(int change) {
        for (String mouseWheelCommand: mouseWheelCommands) {
            inputHandler.handleMeasurableCommand(mouseWheelCommand, change);
        }
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        inputHandler.handleMousePosition(x, y);
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        List<String> commands = mouseButtonToCommands.get(button + 1);
        if (commands != null) {
            for (String command: commands) {
                inputHandler.handleCommand("+"+command);
            }
        }
        inputHandler.handleMousePosition(x, y);
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        List<String> commands = mouseButtonToCommands.get(button + 1);
        if (commands != null) {
            for (String command: commands) {
                inputHandler.handleCommand("-"+command);
            }
        }
        inputHandler.handleMousePosition(x, y);
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        inputHandler.handleMousePosition(newx, newy);
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        inputHandler.handleMousePosition(newx, newy);

    }

    @Override
    public void setInput(Input input) {

    }

    @Override
    public boolean isAcceptingInput() {
        return acceptingInput;
    }

    public void setAcceptingInput(boolean acceptingInput) {
        this.acceptingInput = acceptingInput;
    }

    @Override
    public void inputEnded() {

    }

    @Override
    public void inputStarted() {

    }

    @Override
    public void controllerLeftPressed(int controller) {

    }

    @Override
    public void controllerLeftReleased(int controller) {

    }

    @Override
    public void controllerRightPressed(int controller) {

    }

    @Override
    public void controllerRightReleased(int controller) {

    }

    @Override
    public void controllerUpPressed(int controller) {

    }

    @Override
    public void controllerUpReleased(int controller) {

    }

    @Override
    public void controllerDownPressed(int controller) {

    }

    @Override
    public void controllerDownReleased(int controller) {

    }

    @Override
    public void controllerButtonPressed(int controller, int button) {

    }

    @Override
    public void controllerButtonReleased(int controller, int button) {

    }
}
