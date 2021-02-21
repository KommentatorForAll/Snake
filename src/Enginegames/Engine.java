package Enginegames;

import java.util.*;
import java.util.stream.Collectors;

public class Engine {

    public long ttime, tick;
    public double tps;
    public boolean started;
    public List<Tickable> tickables;

    /**
     * Creates a new Enginegames.Engine to help with ticking
     * @param tps amount of ticks per second
     */
    public Engine(double tps) {
        tickables = new ArrayList<>();
        this.tps = tps;
        ttime = System.currentTimeMillis();
        start();
    }

    public void loop() {
        long cur;
        tick = 0;
        while (started) {
            try {
                TickEngine t = new TickEngine(this, tick++);
                t.start();
                if (Main.enableDebug) {
                    cur = System.currentTimeMillis();
                    System.out.println("Enginetick at "+cur+"\ntick length: "+(cur-ttime)+ "\nexpected data:\n  tps:       "+tps+"\n  ttime:     "+1000/tps);
                    ttime = cur;
                }
                Thread.sleep((long) (1000/tps));
                t.join((int)(1000/tps));
            }
            catch (Exception e) {
                Thread.currentThread().interrupt();
                System.err.println("Error while ticking");
                e.printStackTrace();
            }
        }
        if (Main.enableDebug)
        System.out.println("stopped loop");
    }

    public void tick() {
        tickables.forEach(Tickable::_tick);
    }

    public void start() {
        new EngineStarter(this).start();
    }

    public void stop() {
        started = false;
    }

    public void addObject(Tickable obj) {
        tickables.add(obj);
    }

    public <T extends Tickable> void addObjects(Collection<T> objs) {
        tickables.addAll(objs);
    }

    public void removeObject(Tickable obj) {
        tickables.remove(obj);
    }

    public <T extends Tickable> void removeObjects(Collection<T> objs) {
        tickables.removeAll(objs);
    }

    public <T extends Tickable> void removeObjects(Class<T> cls) {
        tickables = tickables.stream().filter(t -> !cls.isInstance(t)).collect(Collectors.toList());
    }

    public double getTps() {
        return tps;
    }

    public double getMspt() {
        return 1000/tps;
    }



    public static class TickEngine extends Thread {
        Engine e;

        public TickEngine(Engine e, long cnt) {
            super("Tick-"+cnt);
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
