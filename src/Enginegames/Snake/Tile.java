package Enginegames.Snake;

import Enginegames.*;

public class Tile extends WorldObj{

    public Tile next;

    public Tile(Tile next) {
        this.next = next;
    }

    @Override
    public void tick() {

    }
}