package Enginegames.Snake;

import Enginegames.Util;
import Enginegames.World;

public class Head extends Enginegames.Snake.Tile {

    public int size;
    public boolean changedDir = false;

    public Head(World world, int x, int y) {
        super(null, 0);
        size = 3;
        world.addObject(this, x, y);
        setImage(Util.loadImageFromAssets("head"));
        Tile mid = new Tile(this, 0,1), tail = new Tile(this,0, 2);
        world.addObject(mid, x, y-1);
        world.addObject(tail, x, y-2);
    }

    public void loop() {
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

    public void keyPressed(int key) {
        switch (key) {
            case 'w':
                if (dir != 2 && dir != 0) {
                    dir = 0;
                    changedDir = true;
                }
                break;
            case 'a':
                if (dir != 1 && dir != 3) {
                    dir = 1;
                    changedDir = true;
                }
                break;

            case 's':
                if (dir != 2 && dir != 0) {
                    dir = 2;
                    changedDir = true;
                }
                changedDir = true;
                break;

            case 'd':
                if (dir != 1 && dir != 3) {
                    dir = 3;
                    changedDir = true;
                }
                changedDir = true;
                break;
        }
    }
}
