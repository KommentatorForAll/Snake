package Enginegames.Snake;

import Enginegames.GameUI;
import Enginegames.World;
import com.sun.nio.sctp.SctpSocketOption;

public class Main {

    World world;
    GameUI window;

    public Main() {
        this(true);
    }

    public Main(boolean isAdContaminated) {
        System.out.println("test");
        world = new World();
        System.out.println("test");
        window = new GameUI("Snake", world.ui, isAdContaminated);

        window.setVisible(true);

    }


}
