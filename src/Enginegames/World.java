package Enginegames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public abstract class World implements Tickable, KeyListener, MouseListener, WindowListener {

    public int pixelSize;
    public int width, height;
    public Collection<WorldObj> objects;
    public Queue<KeyEventInfo> keys = new ConcurrentLinkedQueue<>();
    public Queue<MouseEventInfo> clicks = new ConcurrentLinkedQueue<>();
    public static Engine e = new Engine(20);
    public boolean hardEdge;
    public WorldUI ui;
    public static GameUI mainframe;
    public Random random;
    public boolean enableDebug = false;
    private int sw, sh;


    /**
     * generates a standard world of 10x10 fields, 16 pixel width/height each.
     */
    public World() {
        this(10, 10, 16);
    }

    /**
     * generates a new world.
     * @param width amount of fields in the x axis
     * @param height amount of fields at the y axis
     * @param pixelSize the size of each field.
     */
    public World(int width, int height, int pixelSize) {
        if(width <= 0 || height <= 0 || pixelSize <= 0) throw new IllegalArgumentException("Size must not be less or equal 0");
        this.width = width;
        this.height = height;
        this.pixelSize = pixelSize;
        updateSW();
        random = new Random();
        objects = new CopyOnWriteArrayList<>();
        ui = new WorldUI(pixelSize*width, pixelSize*height, WorldUI.ImageOption.NONE, pixelSize, this);
        start();
    }

    /**
     * sets the image option of the background
     * @param io the option which gets selected
     */
    public final void setBackgroundOption(WorldUI.ImageOption io) {
        ui.bgOption = io;
    }


    /**
     * resizes the world
     * @param width new width of the world
     * @param height new height of the world
     */
    public final void resizeField(int width, int height) {
        if(width <= 0 || height <= 0 || pixelSize <= 0) throw new IllegalArgumentException("Size must not be less or equal 0");
        this.width = width;
        this.height = height;
        updateSW();
        updateSize();
    }

    /**
     * sets the new fieldsize
     * @param pixelSize the size of each field
     */
    public final void resizeField(int pixelSize) {
        if(width <= 0 || height <= 0 || pixelSize <= 0) throw new IllegalArgumentException("Size must not be less or equal 0");
        this.pixelSize = pixelSize;
        ui.pxsize = pixelSize;
        updateSW();
        updateSize();
    }

    public final void resizeField(double amount) {
        width *= amount;
        height *= amount;
        if (((width*pixelSize > sw || height*pixelSize > sh)&&amount>1) || ((sw > (width+1)*pixelSize || sh > (height+1)*pixelSize)&&amount <1))
            pixelSize /= amount;
        ui.pxsize = pixelSize;
        updateSize();
    }

    /**
     *updates the oveerall size of the world.
     */
    public final void updateSize() {
        if(width <= 0 || height <= 0 || pixelSize <= 0) throw new IllegalArgumentException("Size must not be less or equal 0");
        ui.setSize(width*pixelSize, height*pixelSize);
        ui.repaint();
    }

    private void updateSW() {
        sw = width*pixelSize;
        sh = height*pixelSize;
    }

    /**
     * gets called every tick. this is the super version of the tick method, calls tick()
     */
    public final void _tick() {
        tick();
        handleKeys();
        handleMouse();
        objects.forEach(o -> {if (o.world != null) o._tick();});
        ui.paint(objects);
    }

    /**
     * the tick method, which gets called every tick. has to be overwritten
     */
    public abstract void tick();

    /**
     * adds an object to the world, at the given position
     * @param obj the object which should be added
     * @param x the x position of the object
     * @param y the y position of the object
     */
    public final void addObject(WorldObj obj, int x, int y) {
        objects.add(obj);
        obj.setLocation(x,y);
        obj.world = this;
    }

    /**
     * removes an object from the world
     * @param obj the object to remove
     */
    public final void removeObject(WorldObj obj) {
        objects.remove(obj);
        obj.world = null;
    }

    /**
     * removes all objects of the collection
     * @param objs the objects to remove
     */
    public final <T extends WorldObj> void removeObjects(Collection<T> objs) {
        objects.removeAll(objs);
        objs.forEach(o -> o.world = null);
    }

    /**
     * deltes all objects from the world
     */
    public final void removeAll() {
        objects.forEach(o -> o.world = null);
        objects.clear();
    }

    /**
     * sets the ticks per second to the given value
     * @param tps the tps which are gonna be run
     */
    public static void setTps(double tps) {
        if (tps <= 0) throw new IllegalArgumentException("Tps must not be less or equal 0");
        e.tps = tps;
    }

    /**
     * starts the engine
     */
    public final void start() {
        e.addObject(this);
    }

    /**
     * stops the engine
     */
    public final void stop() {
        e.removeObject(this);
    }

    /**
     * switches the active world
     * @param world the world to be switched to
     */
    public static List<World> switchWorld(World world) {
        List<World> olds = e.tickables.stream().filter(t -> t instanceof World).map(t -> (World) t).collect(Collectors.toList());
        e.removeObjects(World.class);
        e.addObject(world);
        switchFocus(world);
        return olds;
    }

    public static void switchFocus(World world) {
        mainframe.switchWorld(world);
    }

    /**
     * sets the background image
     * @param img the new background
     */
    public final void setBackground(AdvancedImage img) {
        ui.setBackground(img);
    }

    /**
     * Returns if an object is exactly at the given position
     * @param x x position
     * @param y y position
     * @return if an object is exactly at x, y
     */
    public final boolean isObjectAt(int x, int y) {
        if (Main.enableDebug) {
            System.out.println("searching at "+x+","+y);
            objects.forEach(System.out::println);
        }
        return objects.stream().anyMatch((o) -> o.x == x && o.y == y);
    }

    /**
     * returns if an object of the given class is exactly at the position
     * @param x x position
     * @param y y position
     * @param cls class of objects to check for
     */
    public final <T extends WorldObj> boolean isObjectAt(int x, int y, Class<T> cls) {
        return objectsOf(cls).stream().anyMatch(o -> o.x == x && o.y ==y);
    }

    /**
     * returns a list of all objects of the given class
     * @param cls the class all fetched objects are from
     * @return a list of objects
     * @throws NullPointerException when cls is null
     */
    public final <T extends WorldObj> List<T> objectsOf(Class<T> cls) {
        if (cls == null) return (List<T>) objects;
        return objects.stream().filter(cls::isInstance).map(o -> (T) o).collect(Collectors.toList());
    }

    /**
     * returns all objects at the given position
     * @param x x position
     * @param y y position
     * @param cls class to check for
     * @return list of objects
     */
    public final <T extends WorldObj> List<T> objectsAt(int x, int y, Class<T> cls) {
        return objectsOf(cls).stream().filter(o -> o.x == x && o.y == y).collect(Collectors.toList());
    }


    @Override
    public final void keyTyped(KeyEvent e) {
        keys.add(new KeyEventInfo(e, 0));
    }

    @Override
    public final void keyPressed(KeyEvent e) {
        keys.add(new KeyEventInfo(e, 1));
    }

    @Override
    public final void keyReleased(KeyEvent e) {
        keys.add(new KeyEventInfo(e, 2));
    }

    public final void handleKeys() {
        KeyEventInfo e;
        while ((e = keys.poll()) != null)
        {
            switch (e.type) {
                case 0:
                    keyTyped(e.e.getKeyChar());
                    keyTyped((int)e.e.getKeyChar());
                    break;
                case 1:
                    keyPressed(e.e.getKeyChar());
                    keyPressed((int)e.e.getKeyChar());
                    KeyEventInfo finalE = e;
                    objectsOf(Textfield.class).stream().filter(tf -> tf.isSelected).forEach(tf -> tf.keyTyped(finalE.e.getKeyChar()));
                    break;
                case 2:
                    keyReleased(e.e.getKeyChar());
                    keyReleased((int)e.e.getKeyChar());
                    break;
            }
        }
    }


    /**
     * event called, when a key is typed
     * @param key the char of the key
     */
    public void keyTyped(char key){}

    /**
     * event called, when a key is pressed
     * @param key the char of the key
     */
    public void keyPressed(char key){}

    /**
     * event called, when a key is released
     * @param key the char of the key
     */
    public void keyReleased(char key){}

    /**
     * event called, when a key is typed
     * @param key the ascii value of the key
     */
    public void keyTyped(int key){}

    /**
     * event called, when a key is pressed
     * @param key the ascii value of the key
     */
    public void keyPressed(int key){}

    /**
     * event called, when a key is released
     * @param key the ascii value of the key
     */
    public void keyReleased(int key){}

    public final void mousePressed(MouseEvent e) {
        clicks.add(new MouseEventInfo(e, MouseEventInfo.MOUSE_PRESSED));
    }

    public void mousePressed(MouseEvent e, WorldObj obj) {}


    public final void mouseClicked(MouseEvent e) {
        clicks.add(new MouseEventInfo(e, MouseEventInfo.MOUSE_CLICKED));
    }

    public void mouseClicked(MouseEvent e, WorldObj obj) {}


    public final void mouseReleased(MouseEvent e) {
        clicks.add(new MouseEventInfo(e, MouseEventInfo.MOUSE_RELEASED));
    }

    public void mouseReleased(MouseEvent e, WorldObj obj) {}

    public final void mouseEntered(MouseEvent e) {
        clicks.add(new MouseEventInfo(e, MouseEventInfo.MOUSE_ENTERED));
    }

    public void mouseEntered(MouseEvent e, WorldObj obj) {}

    public final void mouseExited(MouseEvent e) {
        clicks.add(new MouseEventInfo(e,MouseEventInfo.MOUSE_EXITED));
    }

    public void mouseExited(MouseEvent e, WorldObj obj) {}

    public void handleMouse() {
        MouseEventInfo e;
        Point p, uiLocation = ui.getLocation();
        //int[] aabsPos, relativePos;
        WorldObj o;
        List<WorldObj> objs;
        while ((e = clicks.poll()) != null)
        {
            p = e.e.getPoint();
            int[] absPos = new int[] {p.x-uiLocation.x, p.y - uiLocation.y};
            int[] relativePos = new int[] {absPos[0]/pixelSize, absPos[1]/pixelSize};
            objs = objects.stream().filter(ob -> ob.isAt(absPos[0], absPos[1], true)).collect(Collectors.toList());
            o = objs.isEmpty()? null : objs.get(0);
            MouseEventInfo finalE = e;
            switch (e.type) {
                case MouseEventInfo.MOUSE_PRESSED:
                    mousePressed(e.e, o);
                    break;
                case MouseEventInfo.MOUSE_CLICKED:
                    mouseClicked(e.e, o);
                    break;
                case MouseEventInfo.MOUSE_RELEASED:
                    mouseReleased(e.e, o);
                    break;
                case MouseEventInfo.MOUSE_ENTERED:
                    mouseEntered(e.e, o);
                    break;
                case MouseEventInfo.MOUSE_EXITED:
                    mouseExited(e.e, o);
            }
            objs.forEach(ob -> ob._mouseEvent(finalE));
        }
    }


    /**
     * sets the order in which the objects are getting painted.
     * @param classes the classes
     */
    @SafeVarargs
    public final void setPaintOrder(Class<? extends WorldObj> ... classes) {
        ui.setPaintOrder(classes);
    }

    /**
     * returns the offset, due to the bar at the top of a jframe
     * @return the offset (to add to the point you are looking for) as a Point
     */
    public final Point getOffset() {
        Point p = ui.getLocation();
        int[] bardim = mainframe.bardim;
        p.x += bardim[0]+(pixelSize/2);
        p.y += bardim[1]+(pixelSize/2);
        return p;
    }

    private static class KeyEventInfo {
        public int type;
        public KeyEvent e;
        public KeyEventInfo(KeyEvent e, int type) {
            this.e = e;
            this.type = type;
        }
    }

    /**
     * sets the background opaqueness of the world. a value less than 1 results in a fading of the drawn images
     * @param opaque opaqueness to set to
     * @throws IllegalArgumentException if the opaquness is not between (inclusive) 0 and 1
     */
    public final void setBackgroundOpaqueness(double opaque) {
        if (!((0<= opaque) && (opaque <= 1))) throw new IllegalArgumentException("Opaqueness has to be between 0 and 1");
        ui.setBackgroundOpaqueness(opaque);
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

    /**
     * called as a WindowListener, can be implemented for initial or final actions
     */
    public void windowOpened(WindowEvent e) {}
    /**
     * called as a WindowListener, can be implemented for initial or final actions
     */
    public void windowClosed(WindowEvent e) {}
    /**
     * called as a WindowListener, can be implemented for initial or final actions
     */
    public void windowClosing(WindowEvent e) {}
    /**
     * called as a WindowListener, can be implemented for initial or final actions
     */
    public void windowIconified(WindowEvent e) {}
    /**
     * called as a WindowListener, can be implemented for initial or final actions
     */
    public void windowDeiconified(WindowEvent e) {}
    /**
     * called as a WindowListener, can be implemented for initial or final actions
     */
    public void windowActivated(WindowEvent e) {}
    /**
     * called as a WindowListener, can be implemented for initial or final actions
     */
    public void windowDeactivated(WindowEvent e) {}
}
