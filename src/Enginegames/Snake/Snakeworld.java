package Enginegames.Snake;

import Enginegames.*;

public class Snakeworld extends World {

    public int t = 0;
    public boolean started = false, running = false, wasDead, endless=true;
    public Head head;
    public double sizingBorder = .75;

    public Snakeworld() {
        super(10, 10, 64);
        Enginegames.Main.enableDebug = true;
        Button b = new Label();
        addObject(b, 1,1);
        setTps(8);
        setPaintOrder(Head.class, Tile.class, Apple.class, Button.class);
        setBackground(Utils.loadImageFromAssets("background_tile"));
        //setBackground(Utils.loadImageFromAssets("invis"));
        //setBackground(null);
        setBackgroundOption(WorldUI.ImageOption.TILED);
        setBackgroundOpaqueness(0.125);
    }

    public void showDeathscreen() {
        System.out.println("you died");
        wasDead = true;
        removeAll();
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
