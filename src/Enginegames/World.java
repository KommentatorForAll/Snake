package Enginegames;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public abstract class World implements Tickable, KeyListener, MouseListener {

    public int pixelSize;
    public int width, height;
    public ArrayList<WorldObj> objects, toRemove = new ArrayList<>(), toAdd = new ArrayList<>();
    public Queue<KeyEventInfo> keys = new ConcurrentLinkedQueue<>();
    public Queue<MouseEventInfo> clicks = new ConcurrentLinkedQueue<>();
    public static Engine e = new Engine(20);
    public boolean hardEdge;
    public WorldUI ui;
    public Random random;
    public boolean enableDebug = false;


    public World() {
        this(10, 10, 16);
    }
    public World(int width, int height, int pixelSize) {
        this.width = width;
        this.height = height;
        this.pixelSize = pixelSize;
        random = new Random();
        objects = new ArrayList<>();
        ui = new WorldUI(pixelSize*width, pixelSize*height, WorldUI.ImageOption.NONE, pixelSize, this);
        start();
    }

    public final void setBackgroundOption(WorldUI.ImageOption io) {
        ui.bgOption = io;
    }


    public final void resizeField(int width, int height) {
        this.width = width;
        this.height = height;
        updateSize();
    }

    public final void resizeField(int pixelSize) {
        this.pixelSize = pixelSize;
        updateSize();
    }

    public final void updateSize() {
        ui.setSize(width*pixelSize, height*pixelSize);
        ui.repaint();
    }


    public final void _tick() {
        toRemove.clear();
        toAdd.clear();
        tick();
        handleKeys();
        handleMouse();
        objects.forEach(WorldObj::_tick);
        objects.removeAll(toRemove);
        objects.addAll(toAdd);
        ui.paint(objects);
    }

    public abstract void tick();

    public final void addObject(WorldObj obj, int x, int y) {
        toAdd.add(obj);
        obj.setLocation(x,y);
        obj.world = this;
    }

    public final void removeObject(WorldObj obj) {
        toRemove.add(obj);
        obj.world = null;
    }

    public final <T extends WorldObj> void removeObjects(Collection<T> objs) {
        objects.removeAll(objs);
        objs.forEach(o -> o.world = null);
    }

    public final void removeAll() {
        objects.forEach(o -> o.world = null);
        objects.clear();
    }

    public final void setTps(double tps) {
        e.tps = tps;
    }

    public final void start() {
        e.addObject(this);
    }

    public final void stop() {
        e.removeObject(this);
    }

    public static void switchWorld(World world) {
        e.removeObjects(World.class);
        e.addObject(world);
    }

    public final void setBackground(AdvancedImage img) {
        ui.setBackground(img);
    }

    public final boolean isObjectAt(int x, int y) {
        if (Main.enableDebug) {
            System.out.println("searching at "+x+","+y);
            objects.forEach(System.out::println);
        }
        return objects.stream().anyMatch((o) -> o.x == x && o.y == y);
    }

    public final <T extends WorldObj> boolean isObjectAt(int x, int y, Class<T> cls) {
        return objectsOf(cls).stream().anyMatch(o -> o.x == x && o.y ==y);
    }

    public final <T extends WorldObj> List<T> objectsOf(Class<T> cls) {
        return objects.stream().filter(cls::isInstance).map(o -> (T) o).collect(Collectors.toList());
    }

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
                    break;
                case 2:
                    keyReleased(e.e.getKeyChar());
                    keyReleased((int)e.e.getKeyChar());
                    break;
            }
        }
    }


    public void keyTyped(char key){}

    public void keyPressed(char key){}

    public void keyReleased(char key){}

    public void keyTyped(int key){}

    public void keyPressed(int key){}

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
        int[] absPos, relativePos;
        WorldObj o;
        List<WorldObj> objs;
        while ((e = clicks.poll()) != null)
        {
            p = e.e.getPoint();
            absPos = new int[] {p.x-uiLocation.x, p.y - uiLocation.y};
            relativePos = new int[] {absPos[0]/pixelSize, absPos[1]/pixelSize};
            objs = objectsAt(relativePos[0], relativePos[1], WorldObj.class);
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
            objs.forEach(ob -> ob.mouseEvent(finalE));
        }
    }


    @SafeVarargs
    public final <T extends WorldObj> void setPaintOrder(Class<T>... classes) {
        ui.setPaintOrder(classes);
    }

    private static class KeyEventInfo {
        public int type;
        public KeyEvent e;
        public KeyEventInfo(KeyEvent e, int type) {
            this.e = e;
            this.type = type;
        }
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
