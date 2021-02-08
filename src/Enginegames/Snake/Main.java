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
        window = new GameUI("Snake", world.ui, isAdContaminated);

        window.setVisible(true);

    }
}
