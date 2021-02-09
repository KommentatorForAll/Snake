package Enginegames.Snake;

import Enginegames.*;

public class Snakeworld extends World {

    public Snakeworld() {
        super("Snake", 10, 10, 64);
        setTps(2);
        setBackground(Util.loadImageFromAssets("background_tile"));
        setBackgroundOption(WorldUI.ImageOption.TILED);

        Head head = new Head();
        Tile mid = new Tile(head,0), tail = new Tile(head,0);
        addObject(tail, 5,5);
        addObject(mid, 5,6);
        addObject(head,5,7);

        start();
    }
}
