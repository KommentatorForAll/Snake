package Enginegames.Snake;

import Enginegames.Utils;
import Enginegames.World;
import Enginegames.WorldObj;

public class Apple extends WorldObj {

    public Apple() {
        setImage(Utils.loadImageFromAssets("apple"));
    }

    @Override
    public void tick() {

    }

    public static void generate(World world) {
        int x, y;
        do {
            x = world.random.nextInt(world.width);
            y = world.random.nextInt(world.height);
        } while(world.isObjectAt(x,y));
        world.addObject(new Apple(), x, y);
    }
}