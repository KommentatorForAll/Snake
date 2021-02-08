package Enginegames;

import java.util.*;

public class Engine {

    public double tps;
    public boolean started;
    public ArrayList<Tickable> tickables;

    /**
     * Creates a new Enginegames.Engine to help with ticking
     * @param tps amount of ticks per second
     */
    public Engine(double tps) {
        tickables = new ArrayList<>();
        this.tps = tps;
    }

    public void loop() {
        while (started) {
            try {
                TickEngine t = new TickEngine(this);
                t.start();
                Thread.sleep((long) (1000/tps));
                t.join((int)(1000/tps));
            }
            catch (Exception e) {
                Thread.currentThread().interrupt();
                System.err.println("Error while ticking");
                e.printStackTrace();
            }

        }
        System.out.println("stopped loop");
    }

    public void tick() {
        tickables.forEach(Tickable::tick);
    }

    public void start() {
        new EngineStarter(this).run();
    }

    public void stop() {
        started = false;
    }

    public void addObject(Tickable obj) {
        tickables.add(obj);
    }

    public static class TickEngine extends Thread {
        Engine e;

        public TickEngine(Engine e) {
            this.e = e;
        }

        public void run() {
            e.tick();
        }
    }

    private static class EngineStarter extends Thread {

        Engine e;

        public EngineStarter(Engine e) {
            this.e = e;
        }

        public void run() {
            e.started = true;
            e.loop();
        }
    }
}
