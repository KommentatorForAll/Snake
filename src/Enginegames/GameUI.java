package Enginegames;

import javax.swing.*;
import java.awt.*;
import Enginegames.*;

public class GameUI extends JFrame {

    public boolean adContaminated;

    public GameUI(String name, World world) {
        this(name, world.ui);
    }

    public GameUI(String name, JPanel worldUI) {
        this(name, worldUI, true);
    }

    public GameUI(String name, World world, boolean adContaminated) {
        this(name, world.ui, adContaminated);
    }

    public GameUI(String name, JPanel worldUI, boolean adContaminated) {
        super(name);
        System.out.println("here");
        setLayout(null);
        int x = worldUI.getWidth(), y = worldUI.getHeight();
        if (adContaminated) {
            x += 600;
            y += 200;
        }

        setSize(x,y);
        setLocation(0,0);
        add(worldUI, BorderLayout.CENTER);
        this.adContaminated = adContaminated;
        setVisible(true);
    }
}
