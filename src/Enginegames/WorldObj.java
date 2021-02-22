package Enginegames;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public abstract class WorldObj implements Tickable, Comparable<WorldObj> {

    public int x, y;
    public AdvancedImage img;
    public World world;

    public WorldObj() {
        img = Utils.loadImageFromAssets("Invis");
    }

    public WorldObj(AdvancedImage img) {
        this.img = img;
    }

    public final void setX(int x) {
        this.x = world!=null?world.hardEdge?Math.max(0,Math.min(x,world.width)):x:x;
    }

    public final void setY(int y) {
        this.y = world!=null?world.hardEdge?Math.max(0,Math.min(y,world.width)):y:y;
    }

    public final void setLocation(int x, int y) {
        setX(x);
        setY(y);
    }

    public final void move(int[] dir) throws IndexOutOfBoundsException{
        move(dir[0], dir[1]);
    }

    public final void move(int x, int y) {
        setX(this.x + x);
        setY(this.y + y);
    }

    public final void setImage(AdvancedImage img) {
        this.img = img;
    }

    public final void setImage(String name) throws IOException {
        img = new AdvancedImage(ImageIO.read(new File(name)));
    }

    public void mouseEvent(MouseEventInfo e) {}

    public final void _tick() {
        tick();
    }

    public abstract void tick();

    public final boolean isAtEdge() {
        return x == 0 || x == world.width-1 || y == 0 || y == world.height-1;
    }

    public final boolean oob() {return isOutOfBounds();}

    public final boolean isOutOfBounds() {
        return x < 0 || x >= world.width || y < 0 || y >= world.height;
    }

    public String toString() {
        return super.toString() + "; in world: " + (world==null? "none" : world + "; at position x="+x+", y="+y);
    }

    public int compareTo(WorldObj obj) {
        return 0;
    }
}
