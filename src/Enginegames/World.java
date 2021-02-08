package Enginegames;

import javax.swing.*;
import java.util.*;

public abstract class World implements Tickable {

    public int pixelSize;
    public int width, height;
    public ArrayList<WorldObj> objects;
    public Engine e;
    public WorldUI ui;


    public World() {
        this("");
    }
    public World(String name) {
        pixelSize = 64;
        width = 10;
        height = 10;
        objects = new ArrayList<>();
        ui = new WorldUI(name, pixelSize*width, pixelSize*height);
        e = new Engine(2);
        e.addObject(this);
        e.start();
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
        redraw();
    }

    public void redraw() {

    }

    public void tick() {
        //System.out.println("tick at "+System.currentTimeMillis());
        objects.forEach(WorldObj::tick);
    }

    public void addObject(WorldObj obj, int x, int y) {
        objects.add(obj);
        obj.world = this;
        ui.addImage(obj, new int[] {x,y});
    }
}
