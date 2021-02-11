package Enginegames.Snake;

import Enginegames.Util;
import Enginegames.World;

public class Head extends Enginegames.Snake.Tile {

    public int size;

    public Head(World world, int x, int y) {
        super(null, 0);
        size = 3;
        world.addObject(this, x, y);
        setImage(Util.loadImageFromAssets("head"));
        Tile mid = new Tile(this, 0,1), tail = new Tile(this,0, 2);
        world.addObject(mid, x, y-1);
        world.addObject(tail, x, y-2);
    }

    public void tick() {
        age = 0;

    }
}
