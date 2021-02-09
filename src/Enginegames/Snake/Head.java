package Enginegames.Snake;

import Enginegames.Util;

public class Head extends Enginegames.Snake.Tile {

    public int size;

    public Head() {
        super(null, 0);
        size = 30;
        setImage(Util.loadImageFromAssets("head"));
    }

    public void tick() {
        age = 0;
    }
}
