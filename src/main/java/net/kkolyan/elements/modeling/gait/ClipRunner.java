package net.kkolyan.elements.modeling.gait;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

/**
 * @author nplekhanov
 */
public class ClipRunner {

    public static void run(final Clip clip, final Color background, final double fps) {
        final Set<Integer> pressedKeys = new HashSet<>();
        try {
            clip.init();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        final Component component = new Component() {
            int n;
            @Override
            public void paint(Graphics g) {

                Graphics2D canvas = (Graphics2D) g;
                canvas.setColor(background);
                canvas.fillRect(0, 0, getWidth(), getHeight());

                if (clip instanceof AdjustableClip) {
                    int tx = 0;
                    int ty = 0;
                    if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
                        tx --;
                    }
                    if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
                        tx ++;
                    }
                    if (pressedKeys.contains(KeyEvent.VK_UP)) {
                        ty --;
                    }
                    if (pressedKeys.contains(KeyEvent.VK_DOWN) ){
                        ty ++;
                    }
                    if (tx != 0 || ty != 0) {
                        ((AdjustableClip) clip).moveCursor(tx, ty);
                    }
                }

                clip.updateAndRender(canvas, n);
                n++;
                if (n >= clip.getLength()) {
                    n = 0;
                }

                canvas.setColor(Color.WHITE);
                canvas.drawString("frame: "+n, 20, 20);

                canvas.setColor(Color.red);
                canvas.setStroke(new BasicStroke(1));
                canvas.drawRect(0, 0, clip.getSize().width, clip.getSize().height);
            }
        };
        final JFrame frame = new JFrame();
        frame.add(component);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(clip.getSize().width + 30, clip.getSize().height + 50);
        frame.setVisible(true);
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (frame.isVisible()) {
                        component.repaint();
                        Thread.sleep((long) (1000.0 / fps));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Canvas updater");
        thread.start();
    }
}
