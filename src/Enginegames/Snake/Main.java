package Enginegames.Snake;

import Enginegames.*;

public class Main {

    World world;
    GameUI window;

    public Main() {
        this(true);
    }

    public Main(boolean isAdContaminated) {
        System.out.println("test");
        world = new Snakeworld();
        System.out.println("test");
        window = new GameUI("Snake", world.ui, isAdContaminated, world);

        window.setVisible(true);

    }
}
