package Enginegames;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public abstract class World implements Tickable, KeyListener {

    public int pixelSize;
    public int width, height;
    public ArrayList<WorldObj> objects, toRemove = new ArrayList<>(), toAdd = new ArrayList<>();
    public Queue<KeyEventInfo> keys = new ConcurrentLinkedQueue<>();
    public Engine e;
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
        e = new Engine(20);
        e.addObject(this);
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

    public final void setTps(double tps) {
        e.tps = tps;
    }

    public final void start() {
        if (!e.started) e.start();
    }

    public final void stop() {
        if (e.started) e.stop();
    }

    public final void setBackground(BufferedImage img) {
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
