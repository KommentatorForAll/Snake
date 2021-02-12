package Enginegames.Snake;

import Enginegames.*;

public class Snakeworld extends World {

    public int t = 0;
    public boolean started = false, running = false;
    public Head head;

    public Snakeworld() {
        super(10, 10, 64);
        Enginegames.Main.enableDebug = true;
        setTps(2);
        setBackground(Util.loadImageFromAssets("background_tile"));
        setBackgroundOption(WorldUI.ImageOption.TILED);
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
        System.out.println("Key pressed: "+key);
        if (key == ' ') {
            if (head == null) begin();
            else resume();
        }
        if (key == 27) { //escape
            pause();
        }
        if (head != null) head.keyPressed(key);
    }
}
