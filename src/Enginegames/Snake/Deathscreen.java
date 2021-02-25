package Enginegames.Snake;

import Enginegames.*;
import Enginegames.Button;
import Enginegames.Label;

import java.awt.*;

public class Deathscreen extends World {

    public static Color transparent = new Color(0,0,0,0);

    public World origin;

    public Deathscreen(Tag stats, int score, String gamename, String playername, World origin) {
        super(10,10,100);
        this.origin = origin;
        Label yd = new Label("YOU DIED");
        yd.setFont(Snakeworld.fonts.get("pixel-bubble").deriveFont(30F));
        yd.setTextColor(Color.RED);
        yd.setBackgroundColor(transparent);
        yd.setBorderColor(transparent);
        addObject(yd, 5,2);
        AdvancedImage img = new AdvancedImage(1000,1000);
        img.fill(Color.BLACK);
        setBackground(img);
        setBackgroundOption(WorldUI.ImageOption.STRETCHED);
        boolean ishighscore = Filework.checkHighscore(stats, gamename, score, playername);
        Label nhs = new Label("Highscore: "+ Filework.getHighscore(stats, gamename));
        if (ishighscore) {
            nhs.setText("New Highscore!");
        }
        addObject(nhs, 5, 4);
        Label ys = new Label("Your score: "+ score);
        ys.setBorderColor(Color.GREEN);
        addObject(ys, 5,3);
        Label avgscore = new Label("Average score: " + Filework.getAvgscore(stats, gamename));
        addObject(avgscore, 5,5);
        //switchWorld(this);

        addObject(new Button() {

            @Override
            public void tick() {

            }

            @Override
            public void clickEvent() {
                World.switchWorld(((Deathscreen) world).origin);
            }
        }, 2, 7);
    }

    @Override
    public void tick() {

    }

    public void keyTyped(int key) {
        //switchWorld(origin);
    }
}
