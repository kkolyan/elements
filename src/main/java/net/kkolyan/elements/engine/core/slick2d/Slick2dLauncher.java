package net.kkolyan.elements.engine.core.slick2d;

import net.kkolyan.elements.engine.core.Application;
import net.kkolyan.elements.engine.core.Debug;
import net.kkolyan.elements.engine.core.Reflection;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.SlickException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;

/**
 * @author nplekhanov
 */
public class Slick2dLauncher implements Shell {
    private ApplicationAdapter applicationAdapter = new ApplicationAdapter(this);
    private AppGameContainer appgc;
    private boolean fullscreen;
    private Dimension screenSize;
    private Application application;
    private String title;
    private int fps = -1;

    private MultiValueMap<String,String> aliases = new LinkedMultiValueMap<>();

    public void setTitle(String title) {
        this.title = title;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void start() {
        try {
            applicationAdapter.setApplication(application);
            applicationAdapter.setTitle(title);
            Game game = new ShellAdapter(this, applicationAdapter);
            appgc = new AppGameContainer(game);
            if (screenSize != null || fullscreen) {
                updateDisplayMode();
            }
            if (fps != -1) {
                appgc.setTargetFrameRate(fps);
            }
            appgc.start();
        } catch (SlickException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean isDebug() {
        return Debug.isEnabled();
    }

    public void setDebug(boolean debug) {
        Debug.setEnabled(debug);
    }

    public void setSetting(String name, Object value) {
        Reflection.setProperty(this, name, value);
    }

    public Object getSetting(String name) {
        return Reflection.getProperty(this, name);
    }

    public void setFps(int fps) {
        this.fps = fps;
        if (appgc != null) {
            appgc.setTargetFrameRate(fps);
        }
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        updateDisplayMode();
    }

    public void setScreenSize(Dimension screenSize) {
        this.screenSize = screenSize;
        updateDisplayMode();
    }

    private void updateDisplayMode () {
        if (appgc != null) {
            try {
                appgc.setDisplayMode(screenSize.width, screenSize.height, fullscreen);
            } catch (SlickException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @Override
    public void executeCommand(String command) {
        command = command.trim();
        if (command.isEmpty()) {
            return;
        }
        Collection<String> alias = aliases.get(command);
        if (alias != null) {
            for (String a: alias) {
                executeCommand(a);
            }
        }
        if (command.startsWith("#")) {
            return;
        }
        if (command.startsWith("alias ")) {
            String[] parts = command.split(" ", 3);
            for (String c: parts[2].trim().split(";")) {
                if (!c.isEmpty()) {
                    aliases.add(parts[1].trim(), c.trim());
                }
            }
        }
        if (command.startsWith("set ")) {
            String[] parts = command.split(" ", 3);
            setSetting(parts[1].trim(), parts[2].trim());
        }
        else if (command.startsWith("toggle ")) {
            String[] parts = command.split(" ", 2);
            Object value = getSetting(parts[1]);
            if (value instanceof Boolean) {
                setSetting(parts[1], !(Boolean)value);
            }
            else if (value == null) {
                setSetting(parts[1], true);
            }
            else throw new IllegalStateException("can't toggle value of property '"+parts[1]+"': "+ value);
        }
        else if (command.startsWith("bind ")) {
            String[] parts = command.split(" ", 3);
            bind(parts[1].trim(), parts[2].trim());
        }
        else {
            applicationAdapter.executeCommand(command);
        }
    }

    public void bind(String inputCode, String command) {
        applicationAdapter.bind(inputCode, command);
    }

    public void parseCommandBatch(String resource) {
        try {
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            try {
                Scanner scanner = new Scanner(stream);
                while (scanner.hasNextLine()) {
                    executeCommand(scanner.nextLine().trim());
                }
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
