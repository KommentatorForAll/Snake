package Enginegames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.Arrays;

/**
 * The JFrame the worlds get shown on.
 *
 */
public class GameUI extends JFrame {

    /**
     * If the world contains just way to many ads.
     * Currently not in use. may either be implemented or removed later
     * @deprecated
     */
    public boolean adContaminated;

    /**
     * A list of graphical Devices AKA Monitors connected to the pc.
     */
    public GraphicsDevice[] monitors = getMonitors();

    /**
     * The dimenstion of the bar at the top, showing the minimize, maximize and close Button
     */
    public int[] bardim;

    /**
     * The position of the last monitor
     */
    public int[] mpos;

    /**
     * The currently displayed World
     */
    public World world;

    /**
     * Creates a new UI for a world
     * @param name the name displayed at the taskbar
     * @param world the world
     */
    public GameUI(String name, World world) {
        this(name, world.ui);
    }

    /**
     * Creates a new UI for a jpannel
     * @param name name displayed at the taskbar
     * @param worldUI the worlds UI
     */
    public GameUI(String name, JPanel worldUI) {
        this(name, worldUI, true, null, null);
    }

    /**
     * Creates a new UI for a JPanel
     * @param name the name displayed at the taskbar
     * @param world the world to create the UI for
     * @param adContaminated currently uselsess
     */
    public GameUI(String name, World world, boolean adContaminated) {
        this(name, world.ui, adContaminated, world, world);
    }

    /**
     * Creates a new UI for a world
     * @param name name, displayed at the taskbar
     * @param worldUI the ui of the world
     * @param adContaminated if the world is ad contaminated (useless currently)
     * @param kl the litener, keyevents get redirected to
     * @param ml the listener, mouseevents get redirected to
     */
    public GameUI(String name, JPanel worldUI, boolean adContaminated, KeyListener kl, MouseListener ml) {
        super(name);
        setLayout(null);
        //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        bardim = getBorderDims();
        if (Main.enableDebug)
            System.out.println("Bar dimension: "+Arrays.toString(bardim));
        this.adContaminated = adContaminated;
        switchWorld((World) kl);
        mpos = getLastMonitorPosition();
        setLocation(mpos[0]-worldUI.getWidth()/2,mpos[1]-worldUI.getHeight()/2);
    }

    /**
     * Switches the world to another world
     * @param world The new world to switch to
     */
    public void switchWorld(World world) {
        if (Main.enableDebug) {
            System.out.println("switching world..");
            System.out.println("Old world: " + this.world);
            System.out.println("Switching to: " + world);
        }

        mpos = getLastMonitorPosition();
        World.mainframe = this;
        WorldUI worldUI = world.ui;
        removeMouseListener(this.world);
        removeKeyListener(this.world);
        if (this.world != null)
            remove(this.world.ui);
        addKeyListener(world);
        addMouseListener(world);
        addWindowListener(world);
        this.world = world;
        int x = worldUI.getWidth(), y = worldUI.getHeight();
        if (adContaminated) {
            x += 600;
            y += 200;
        }
        Dimension d = new Dimension(x+bardim[0], y+bardim[1]);
        int w = getWidth(), h = getHeight();
        d = new Dimension(Math.max(d.width, w), Math.max(d.height, h));
        if (Main.enableDebug)
            System.out.println("new Frame dimensions: "+d);
        setMinimumSize(d);
        setSize(d);
        repaint();
        worldUI.setLocation(d.width/2-worldUI.getWidth()/2-bardim[0]/2, d.height/2-worldUI.getHeight()/2-bardim[1]/2);
        System.out.println(worldUI.getLocation());
        add(worldUI);


        addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent evt) {
                int x = getWidth(), y = getHeight();
                worldUI.setLocation(x/2-worldUI.getWidth()/2-(bardim[0]/2), y/2-worldUI.getHeight()/2-(bardim[1]/2));
                setLocation(mpos[0]-worldUI.getWidth()/2,mpos[1]-worldUI.getHeight()/2);
            }
        });

    }

    /**
     * returns a list of monitors, connected to the pc
     * @return a list of graphics devices
     */
    public GraphicsDevice[] getMonitors() {
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();

        if (Main.enableDebug)
        for (GraphicsDevice device : devices) {
            System.out.println("Width:" + device.getDisplayMode().getWidth());
            System.out.println("Height:" + device.getDisplayMode().getHeight());
        }
        return devices;
    }

    /**
     * returns the position of the last moitor as {x, y}
     * @return the positio the last mointor starts at.
     */
    public int[] getLastMonitorPosition() {
        int x = 0;
        int y = 0;
        for (int i = 0; i < monitors.length-1; i++) {
            x += monitors[i].getDisplayMode().getWidth();
        }
        x+= monitors[monitors.length-1].getDisplayMode().getWidth()/2;
        y+= monitors[monitors.length-1].getDisplayMode().getHeight()/2;
        return new int[] {x,y};
    }

    /**
     * returns the dimension of the toolbar.
     * @return dimension of the toolbar
     */
    public int[] getBorderDims() {
        Insets insets = getInsets();
        if (insets != null) {
            return new int[] {insets.left+insets.right, insets.top+insets.bottom};
        }
        return new int[] {0, 0};
    }
}
