package net.kkolyan.elements.engine.core.slick2d;

import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nplekhanov
 */
public class ShellAdapter implements Game, KeyListener {

    private List<String> history = new ArrayList<String>();
    private int historyCursor;
    private String commandLine;
    private Shell shell;

    private SwitchableInputGame target;

    private TrueTypeFont font;

    private Input input;

    public ShellAdapter(Shell shell, SwitchableInputGame target) {
        this.shell = shell;
        this.target = target;
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        font = new TrueTypeFont(new Font("Courier New", Font.BOLD, 16), true);
        target.init(container);
        input = container.getInput();
        input.addKeyListener(this);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        container.setShowFPS(commandLine == null);
        target.update(container, delta);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        target.render(container, g);

        if (commandLine != null) {
            g.setColor(new Color(0, 0, 0, 127));
            g.fillRect(0, 0, container.getWidth(), container.getHeight());
            g.setColor(Color.white);
            g.setFont(font);
            g.drawString(">"+commandLine+"_", 30, 30);
        }
    }

    @Override
    public boolean closeRequested() {
        return target.closeRequested();
    }

    @Override
    public String getTitle() {
        return target.getTitle();
    }

    @Override
    public void keyPressed(int key, char c) {

        if (key != -1)
        {

            // alt and control keys don't come through here
            if (input.isKeyDown(Input.KEY_LCONTROL) || input.isKeyDown(Input.KEY_RCONTROL)) {
                return;
            }
            if (input.isKeyDown(Input.KEY_LALT) || input.isKeyDown(Input.KEY_RALT)) {
                return;
            }
        }

        if (key == Input.KEY_GRAVE) {
            if (commandLine == null) {
                commandLine = "";
            } else {
                commandLine = null;
            }

            target.setAcceptingInput(commandLine == null);
            return;
        }

        if (commandLine == null) {
            return;
        }

        if (key == Input.KEY_UP) {
            navigateHistory(1);
        }
        else if (key == Input.KEY_DOWN) {
            navigateHistory(-1);
        }
        else if (key == Input.KEY_BACK) {
            if (!commandLine.isEmpty()) {
                commandLine = commandLine.substring(0, commandLine.length() - 1);
            }
        } else if ((c < 127) && (c > 31)) {
            commandLine = commandLine + c;
        } else if (key == Input.KEY_RETURN) {
            if (!commandLine.isEmpty()) {
                shell.executeCommand(commandLine);
                history.add(commandLine);
                historyCursor = 0;
                commandLine = "";
            }
        }
    }

    private void navigateHistory(int offset) {
        historyCursor += offset;

        if (historyCursor > history.size()) {
            historyCursor = history.size();
        }
        if (historyCursor < 0) {
            historyCursor = 0;
        }

        if (historyCursor == 0) {
            commandLine = "";
        } else {
            commandLine = history.get(history.size() - historyCursor);
        }
    }

    @Override
    public void keyReleased(int key, char c) {
    }

    @Override
    public void setInput(Input input) {
    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void inputEnded() {
    }

    @Override
    public void inputStarted() {
    }
}
