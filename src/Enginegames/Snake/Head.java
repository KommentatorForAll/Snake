package Enginegames.Snake;

import Enginegames.AdvancedImage;
import Enginegames.Utils;
import Enginegames.World;

import java.awt.image.BufferedImage;

public class Head extends Enginegames.Snake.Tile {

    public int size, lastdir;
    public boolean changedDir = false;
    public static AdvancedImage commonImage = AdvancedImage.rotateImageByDegrees(Utils.loadImageFromAssets("snake_head"), 90);


    public Head(World world, int x, int y) {
        super(null, 0);
        //commonImage.imgpos = AdvancedImage.ImagePosition.TOP_LEFT;
        //commonImage.imgs = AdvancedImage.ImageSizing.TILE;
        size = 2;
        world.addObject(this, x, y);
        commonImage.imgs = AdvancedImage.ImageSizing.STRETCH;
        setImage(commonImage);
        //Tile mid = new Tile(this, 0,1), tail = new Tile(this,0, 2);
        //world.addObject(mid, x, y-1);
        //world.addObject(tail, x, y-2);
        Apple.generate(world);
        dir = 2;
        lastdir = dir;
    }

    public void tick() {
        checkDeath();
        appleHandling();
        changedDir = false;
        int dx = 0, dy = 0;
        switch (dir) {
            case 0:
                dy = -1;
                break;
            case 1:
                dx = -1;
                break;
            case 2:
                dy = 1;
                break;
            case 3:
                dx = 1;
                break;
        }
        world.addObject(new Tile(this, lastdir*4+dir), this.x, this.y);
        move(dx,dy);
        lastdir = dir;
    }

    private void checkDeath() {
        if (oob() || world.objectsAt(x,y, Tile.class).size() >1) {
            ((Snakeworld) world).showDeathscreen();
        }
    }

    public void appleHandling() {
        if (world.isObjectAt(x,y, Apple.class)) {
            size++;
            world.removeObject(world.objectsOf(Apple.class).get(0));
            Apple.generate(world);
            Snakeworld world = (Snakeworld) this.world;
            if (world.sizingBorder == 0) return;
            if (size >= world.sizingBorder*(world.width*world.height)) {
                world.resizeField(world.increase_amount);
            }
        }
    }

    public void keyPressed(int key) {
        if (!changedDir)
        switch (key) {
            case (int)'w':
                if (dir != 2 && dir != 0) {
                    dir = 0;
                    changedDir = true;
                }
                break;
            case (int)'a':
                if (dir != 1 && dir != 3) {
                    dir = 1;
                    changedDir = true;
                }
                break;

            case (int)'s':
                if (dir != 2 && dir != 0) {
                    dir = 2;
                    changedDir = true;
                }
                changedDir = true;
                break;

            case (int)'d':
                if (dir != 1 && dir != 3) {
                    dir = 3;
                    changedDir = true;
                }
                changedDir = true;
                break;
        }
        setImage(AdvancedImage.rotateImageByDegrees(commonImage, dir * 90));
    }
}
