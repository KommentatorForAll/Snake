package Enginegames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;

public class GameUI extends JFrame {

    public boolean adContaminated;
    public GraphicsDevice[] monitors = getMonitors();


    public GameUI(String name, World world) {
        this(name, world.ui);
    }

    public GameUI(String name, JPanel worldUI) {
        this(name, worldUI, true, null);
    }

    public GameUI(String name, World world, boolean adContaminated) {
        this(name, world.ui, adContaminated, null);
    }

    public GameUI(String name, JPanel worldUI, boolean adContaminated, KeyListener kl) {
        super(name);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        int x = worldUI.getWidth(), y = worldUI.getHeight();
        if (adContaminated) {
            x += 600;
            y += 200;
        }
        addKeyListener(kl);

        setSize(x,y);
        setMinimumSize(worldUI.getSize());
        int[] mpos = getLastMonitorPosition();
        setLocation(mpos[0],mpos[1]);
        worldUI.setLocation(x/2-worldUI.getWidth()/2, y/2-worldUI.getHeight()/2);
        add(worldUI);//, BorderLayout.CENTER);
        this.adContaminated = adContaminated;
        setVisible(true);

        addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent evt) {
                int x = getWidth(), y = getHeight();
                worldUI.setLocation(x/2-worldUI.getWidth()/2, y/2-worldUI.getHeight()/2);
            }
        });
    }

    public GraphicsDevice[] getMonitors() {
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();

        for (int i = 0; i < devices.length; i++) {
            System.out.println("Width:" + devices[i].getDisplayMode().getWidth());
            System.out.println("Height:" + devices[i].getDisplayMode().getHeight());
        }
        return devices;
    }

    public int[] getLastMonitorPosition() {
        int x = 100;
        int y = 100;
        for (int i = 0; i < monitors.length-1; i++) {
            x += monitors[i].getDisplayMode().getWidth();
        }
        return new int[] {x,y};
    }
}
