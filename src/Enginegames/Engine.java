package Enginegames;

import java.util.*;
import java.util.stream.Collectors;

public class Engine {

    public long ttime, tick, latestMspt;
    public double tps;
    public boolean started, terminateOnError;
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

    /**
     * Main function of the Engine. Each loop, a new Tickthread is started, Afterwards it waits mspt milliseconds and terminates the thread afterwards. (Support for switching between lag and skipping may be implemented later)
     * Prints error message, when errors occur during the loop, but does not terminate
     */
    public void loop() {
        long cur;
        tick = 0;
        while (started) {
            try {
                TickEngine t = new TickEngine(this, tick++);
                t.start();
                Thread.sleep((long) (1000/tps));
                t.join((int)(1000/tps));
                cur = System.currentTimeMillis();
                latestMspt = cur-ttime;
                ttime = cur;
                if (Main.enableDebug) {
                    System.out.println("Enginetick at "+cur+"\ntick length: "+(latestMspt)+ "\nexpected data:\n  tps:       "+tps+"\n  ttime:     "+1000/tps);
                }
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Error while ticking");;
                e.printStackTrace();
            }
            catch (Exception e) {
                Thread.currentThread().interrupt();
                System.err.println("Error while ticking");;
                if (terminateOnError) throw e;
                e.printStackTrace();
            }
        }
        if (Main.enableDebug)
        System.out.println("stopped loop");
    }

    /**
     * gets called every tick. calls all objects which are said to be executed
     */
    public void tick() {
        tickables.forEach(Tickable::_tick);
    }

    /**
     * starts the engine.
     * !!can create multiple instances running at the same time!!
     */
    public void start() {
        new EngineStarter(this).start();
    }

    /**
     * stops the engine.
     * Consider just removing the World from the engine instead. See World.stop() and/or World.switchWorld(World w)
     */
    public void stop() {
        started = false;
    }

    /**
     * adds an object to the engine which shall be executed
     * @param obj the new object which shall be called every tick
     */
    public void addObject(Tickable obj) {
        tickables.add(obj);
    }

    /**
     * Adds a list of objects to the engine
     * @param objs the list of objects which
     */
    public <T extends Tickable> void addObjects(Collection<T> objs) {
        tickables.addAll(objs);
    }

    /**
     * removes the object from the list of objects which get called every tick
     * @param obj the object which gets removed
     */
    public void removeObject(Tickable obj) {
        tickables.remove(obj);
    }

    /**
     * removes a list of objects from the engine
     * @param objs the list of objects
     */
    public <T extends Tickable> void removeObjects(Collection<T> objs) {
        tickables.removeAll(objs);
    }

    /**
     * removes all objects of the class T
     * @param cls the class these objects are from
     * @param <T> the class t has to implement the tickable interface
     */
    public <T extends Tickable> void removeObjects(Class<T> cls) {
        tickables = tickables.stream().filter(t -> !cls.isInstance(t)).collect(Collectors.toList());
    }

    /**
     * returns the amount of tps or Ticks per Second
     * @return the tps the engine ran on the latest tick
     */
    public double getTps() {
        return 1000.0/latestMspt;
    }

    /**
     * returns the expected amount of milliseconds per tick
     * @return the mspt of the last tick
     */
    public double getMspt() {
        return latestMspt;
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
