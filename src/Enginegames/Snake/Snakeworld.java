package Enginegames.Snake;

import Enginegames.*;
import Enginegames.Button;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class Snakeworld extends World {

    public int t = 0;
    public boolean started = false, running = false, wasDead, endless=true;
    public Head head;
    public Tag stats;
    public double sizingBorder = 0;
    public double increase_amount = 0;
    public double bg_opaqueness = 1;
    public static HashMap<String, Font> fonts = Utils.loadAllFonts();
    public static Gamemode g = Gamemode.DEFAULT;

    public Snakeworld() {
        super(10, 10, 64);
        Enginegames.Main.enableDebug = false;
        stats = Filework.readScores();
        stats.print();
        setTps(8);
        setPaintOrder(Head.class, Tile.class, Apple.class, Button.class);
        setBackground(Utils.loadImageFromAssets("background_tile"));
        //setBackground(Utils.loadImageFromAssets("invis"));
        //setBackground(null);
        setBackgroundOption(WorldUI.ImageOption.TILED);
        setBackgroundOpaqueness(bg_opaqueness);
    }

    public Snakeworld(Gamemode g, Tag stats, AdvancedImage bg_img, double bg_opaqueness) {
        super(g.width, g.height, g.pxsize);
        Enginegames.Main.enableDebug = false;
        Snakeworld.g = g;
        setTps(g.tps);
        setPaintOrder(Head.class, Tile.class, Apple.class, Button.class);
        setBackgroundOption(WorldUI.ImageOption.TILED);
        this.bg_opaqueness = bg_opaqueness;
        this.stats = stats;
        sizingBorder = g.increase_border;
        increase_amount = g.increase_amount;
        stats.print();
        setBackgroundOpaqueness(bg_opaqueness);
        setBackground(bg_img);
        setBackgroundOption(WorldUI.ImageOption.TILED);

    }

    public void showDeathscreen() {
        System.out.println("you died");

        wasDead = true;
        removeAll();
        switchWorld(new Deathscreen(stats, head.size-g.start_size, g.name, "player1", this));
        //setBackground(null);
        //stop();
    }

    public void tick() {
        t++;
        if (started && !running) {
            started = true;
            running = true;
        }
    }

    public void begin() {
        head = new Head(this, width/2,height/2);
        head.size = g.start_size;
        resume();
    }

    public void resume() {
        running = true;
        if (!e.started) start();
    }

    public void pause() {
        running = false;
        stop();
    }

    public void keyPressed(int key) {
        if (Main.enableDebug)
            System.out.println("Key pressed: "+key);
        if (key == ' ') {
            if (head == null) begin();
            else resume();
            if (wasDead) {
                System.out.println("has been dead");
                wasDead = false;
                removeAll();
                begin();
            }
        }
        if (key == 27) { //escape
            pause();
        }
        if (head != null) head.keyPressed(key);
    }

    public void windowClosed(WindowEvent e) {
        Filework.writeScores(stats);
    }

    public static class Gamemode {

        public static Gamemode DEFAULT = new Gamemode("default", 10, 10, 64, 2, 0,0);
        public static Gamemode BIG = new Gamemode("big", 50,25,32,2,0,0);
        public static Gamemode GIANT = new Gamemode("giant", 75, 35,16, 2, 0,0);
        public static Gamemode ENDLESS = new Gamemode("endless", 10,10,64,2,1.1,.75);
        public static Gamemode TRON = new Gamemode("tron", 15,15,64,99999,0,0);
        public static Gamemode CUSTOM = new Gamemode("custom", 0,0,0,0,0,0);
        public static Gamemode[] values = {DEFAULT, BIG, GIANT, ENDLESS, TRON, CUSTOM};

        public int width, height, pxsize;
        public int start_size;
        public double increase_amount, increase_border;
        public double tps = 8;
        public String name;

        Gamemode(String name, int width, int height, int pxsize, int start_size, double increase_amount, double increase_border) {
            this.name = name;
            this.width = width;
            this.height = height;
            this.pxsize = pxsize;
            this.start_size = start_size;
            this.increase_amount = increase_amount;
            this.increase_border = increase_border;
        }

        public static Gamemode from(String name) {
            name = name.toLowerCase();
            for (Gamemode g : Gamemode.values) {
                if (g.name.equals(name)) {
                    return g;
                }
            }
            return CUSTOM;
        }

        public static Gamemode from(int width, int height, int pxsize, int start_size, double increase_amount, double increase_border) {
            for (Gamemode g : Gamemode.values) {
                if (g.width == width && g.height == height && g.pxsize == pxsize && g.start_size == start_size && g.increase_border == increase_border && g.increase_amount == increase_amount)
                    return g;
            }
            return CUSTOM;
        }

    }
}
