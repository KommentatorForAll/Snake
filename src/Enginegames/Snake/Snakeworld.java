package Enginegames.Snake;

import Enginegames.*;

public class Snakeworld extends World {

    public int t = 0;

    public Snakeworld() {
        super("Snake", 10, 10, 64);
        setTps(2);
        setBackground(Util.loadImageFromAssets("background_tile"));
        setBackgroundOption(WorldUI.ImageOption.TILED);

    }

    public void tick() {
        super.tick();
        t++;
        if (t == 10) {
            new Head(this, 5,5);
        }
    }


}
