package Enginegames.Snake;

import Enginegames.Util;
import Enginegames.World;

public class Head extends Enginegames.Snake.Tile {

    public int size;
    public boolean changedDir = false;

    public Head(World world, int x, int y) {
        super(null, 0);
        size = 2;
        world.addObject(this, x, y);
        setImage(Util.loadImageFromAssets("head"));
        Tile mid = new Tile(this, 0,1), tail = new Tile(this,0, 2);
        world.addObject(mid, x, y-1);
        world.addObject(tail, x, y-2);
        Apple.generate(world);
        dir = 2;
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
        world.addObject(new Tile(this, 0), this.x, this.y);
        move(dx,dy);
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
        }
    }

    public void keyPressed(int key) {
        if (!changedDir)
        switch (key) {
            case (int)'w':
                System.out.println("here");
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
    }
}
