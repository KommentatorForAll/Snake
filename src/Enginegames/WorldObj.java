package Enginegames;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class WorldObj implements Tickable{

    public int x, y;
    public Image img;
    public World world;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setLocation(int x, int y) {
        setX(x);
        setY(y);
    }

    public void move(int[] dir) throws IndexOutOfBoundsException{
        move(dir[0], dir[1]);
    }

    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void setImage(Image img) {
        this.img = img;
    }

    public void setImage(String name) throws IOException {

        img = ImageIO.read(new File(name));

    }



    public abstract void tick();



}
