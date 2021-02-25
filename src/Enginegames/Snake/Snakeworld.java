package Enginegames.Snake;

import Enginegames.*;
import Enginegames.Button;

import java.awt.*;
import java.util.HashMap;

public class Snakeworld extends World {

    public int t = 0;
    public boolean started = false, running = false, wasDead, endless=true;
    public Head head;
    public Tag stats;
    public double sizingBorder = .1;
    public static HashMap<String, Font> fonts = Utils.loadAllFonts();

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
        setBackgroundOpaqueness(1);
    }

    public void showDeathscreen() {
        System.out.println("you died");

        wasDead = true;
        removeAll();
        switchWorld(new Deathscreen(stats, head.size-2, "endless", "player1", this));
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
        head = new Head(this, 5,5);
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
}
