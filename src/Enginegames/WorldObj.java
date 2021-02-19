package Enginegames;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public abstract class WorldObj implements Tickable {

    public int x, y;
    public Image img;
    public World world;

    public WorldObj() {
        img = Util.loadImageFromAssets("Invis");
    }

    public WorldObj(Image img) {
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

    public final void setImage(Image img) {
        this.img = img;
    }

    public final void setImage(String name) throws IOException {
        img = ImageIO.read(new File(name));
    }

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
        String s = super.toString();
        s += "; in world: ";
        if (world == null) {
            s+="none";
        }
        else {
            s+= world + "; at position x=" + x + ", y=" + y;
        }
        return s;
    }
}
