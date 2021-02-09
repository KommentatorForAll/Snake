package Enginegames.Snake;

import Enginegames.*;

public class Tile extends WorldObj {

    public Head head;
    public int age = 0;
    public int dir;

    public Tile(Head head, int dir) {
        super();
        this.head = head;
        this.dir = dir;
        setImage(Util.loadImageFromAssets("python"));
    }

    @Override
    public void tick() {
        age++;
        if (age >= head.size) {
            world.removeObject(this);
        }
        if (age == head.size-1) {
            setImage(Util.loadImageFromAssets("tail"));
        }
    }
}
