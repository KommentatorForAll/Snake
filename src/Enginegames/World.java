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
    public ArrayList<WorldObj> objects, toRemove;
    public Engine e;
    public WorldUI ui;


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
        //e.start();
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
        System.out.println("tick at "+System.currentTimeMillis());
        toRemove = new ArrayList<>();
        objects.forEach(WorldObj::tick);
        objects.removeAll(toRemove);
        ui.repaint();
    }

    public void addObject(WorldObj obj, int x, int y) {
        objects.add(obj);
        obj.world = this;
        ui.addImage(obj, new int[] {x,y});
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
