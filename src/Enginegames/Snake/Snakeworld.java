package Enginegames.Snake;

import Enginegames.*;
import Enginegames.Button;
import Enginegames.Label;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class Snakeworld extends World {

    public int t = 0;
    public boolean running, wasDead;
    public Head head;
    public Tag stats;
    public Label l;
    public double sizingBorder;
    public double increase_amount;
    public double bg_opaqueness;
    public static HashMap<String, Font> fonts = Utils.loadAllFonts();
    public static Gamemode g = Gamemode.DEFAULT;

    public Snakeworld(Gamemode g, Tag stats, AdvancedImage bg_img, double bg_opaqueness) {
        super(g.width, g.height, g.pxsize);
        Enginegames.Main.enableDebug = true;
        Snakeworld.g = g;
        setTps(g.tps);
        setPaintOrder(Label.class, Head.class, Tile.class, Apple.class, Button.class);
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
        switchWorld(new Deathscreen(stats, head.size-g.start_size, g.name, this));
    }

    public void tick() {
        t++;
    }

    public void begin() {
        head = new Head(this, width/2,height/2);
        head.size = g.start_size;
        resume();
    }

    public void resume() {
        running = true;
        removeObjects(Label.class);
        System.out.println(l);
    }

    public void pause() {
        running = false;
        l = new Label("PAUSED");
        l.setTextColor(Color.WHITE);
        l.setBackgroundColor(Color.BLACK);
        l.setFont(fonts.get("pixel-bubble").deriveFont(30F));
        addObject(l, width/2, height/2);
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
        System.out.println("saving stats");
        Filework.writeScores(stats);
    }

    public static class Gamemode {

        public static Gamemode DEFAULT = new Gamemode("default", 10, 10, 64, 2, 0,0);
        public static Gamemode CUSTOM = new Gamemode("custom", 0,0,0,0,0,0);
        public static Gamemode[] values = Filework.loadGamemodes().toArray(new Gamemode[0]);

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
    }
}
