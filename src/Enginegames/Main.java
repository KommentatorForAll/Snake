package Enginegames;

import Enginegames.Snake.Snakeworld;

public class Main {

    public static boolean enableDebug = false;

    World world;
    GameUI window;

    public Main() {
        this(false);
    }

    public Main(boolean isAdContaminated) {
        world = new Snakeworld();
        window = new GameUI("Snake", world.ui, isAdContaminated, world);
        window.setIconImage(Util.loadImageFromAssets("title"));
        window.setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
