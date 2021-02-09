package Enginegames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        int x = worldUI.getWidth(), y = worldUI.getHeight();
        if (adContaminated) {
            x += 600;
            y += 200;
        }

        setSize(x,y);
        setMinimumSize(worldUI.getSize());
        setLocation(0,0);
        worldUI.setLocation(x/2-worldUI.getWidth()/2, y/2-worldUI.getHeight()/2);
        add(worldUI);//, BorderLayout.CENTER);
        this.adContaminated = adContaminated;
        setVisible(true);

        addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent evt) {
                Component c = (Component)evt.getSource();
                int x = getWidth(), y = getHeight();
                worldUI.setLocation(x/2-worldUI.getWidth()/2, y/2-worldUI.getHeight()/2);
            }
        });
    }


}
