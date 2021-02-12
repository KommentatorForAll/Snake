package Enginegames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.*;

public abstract class World implements Tickable, KeyListener {

    public int pixelSize;
    public int width, height;
    public ArrayList<WorldObj> objects, toRemove = new ArrayList<>(), toAdd = new ArrayList<>();
    public Engine e;
    public WorldUI ui;
    public boolean enableDebug = false;


    public World() {
        this("", 10, 10, 16);
    }
    public World(String name, int width, int height, int pixelSize) {
        this.width = width;
        this.height = height;
        this.pixelSize = pixelSize;
        objects = new ArrayList<>();
        ui = new WorldUI(name, pixelSize*width, pixelSize*height, WorldUI.ImageOption.NONE, pixelSize, this);
        e = new Engine(20);
        e.addObject(this);
        start();
    }

    public void setBackgroundOption(WorldUI.ImageOption io) {
        ui.bgOption = io;
    }


    public void resizeField(int width, int height) {
        this.width = width;
        this.height = height;
        updateSize();
    }

    public void resizeField(int pixelSize) {
        this.pixelSize = pixelSize;
        updateSize();
    }

    public void updateSize() {
        ui.setSize(width*pixelSize, height*pixelSize);
        ui.repaint();
    }


    public void tick() {
        if (enableDebug)
        System.out.println("tick at "+System.currentTimeMillis());
        objects.removeAll(toRemove);
        objects.addAll(toAdd);
        toRemove = new ArrayList<>();
        toAdd = new ArrayList<>();
        loop();
        objects.forEach(WorldObj::tick);
        ui.paint(objects);
    }

    public abstract void loop();

    public void addObject(WorldObj obj, int x, int y) {
        toAdd.add(obj);
        System.out.println(toAdd.size());
        obj.world = this;
    }

    public void removeObject(WorldObj obj) {
        toRemove.add(obj);
    }

    public void setTps(double tps) {
        e.tps = tps;
    }

    public void start() {
        e.start();
    }

    public void stop() {
        e.stop();
    }

    public void setBackground(BufferedImage img) {
        ui.setBackground(img);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        keyTyped(e.getKeyChar());
        keyTyped(e.getKeyCode());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyPressed(e.getKeyChar());
        keyPressed(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyReleased(e.getKeyChar());
        keyReleased(e.getKeyCode());
    }

    public void keyTyped(char key){}

    public void keyPressed(char key){}

    public void keyReleased(char key){}

    public void keyTyped(int key){}

    public void keyPressed(int key){}

    public void keyReleased(int key){}
}
