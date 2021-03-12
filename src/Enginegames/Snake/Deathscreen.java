package Enginegames.Snake;

import Enginegames.*;
import Enginegames.Button;
import Enginegames.Dialog;
import Enginegames.Label;

import java.awt.*;
import java.awt.event.WindowEvent;

public class Deathscreen extends World {

    public static Color transparent = new Color(0,0,0,0);

    public World origin;
    public static Tag stats = Filework.readScores();

    public Deathscreen(Tag stats, int score, String gamename, World origin) {
        super(10,10,100);
        this.origin = origin;
        Label yd = new Label("YOU DIED");
        yd.setFont(Snakeworld.fonts.get("pixel-bubble").deriveFont(30F));
        yd.setTextColor(Color.RED);
        yd.setBackgroundColor(transparent);
        yd.setBorderColor(transparent);
        Deathscreen.stats = stats;
        addObject(yd, 5,2);
        AdvancedImage img = new AdvancedImage(1000,1000);
        img.fill(Color.BLACK);
        setBackground(img);
        img.imgs = AdvancedImage.ImageSizing.STRETCH;
        boolean ishighscore = Filework.checkHighscore(stats, gamename, score);
        String oldHSH = (String) stats.findNextTag(gamename).get("playername");
        Label nhs = new Label("Highscore\n"+ oldHSH + ": "+Filework.getHighscore(stats, gamename));
        if (ishighscore) {
            String tmp = Dialog.askString("Your name", oldHSH);
            nhs.setText("New Highscore!");
            Filework.setHighscore(score, stats, gamename, tmp);
        }
        addObject(nhs, 5, 4);
        Label ys = new Label("Your score: "+ score);
        ys.setBorderColor(Color.GREEN);
        addObject(ys, 5,3);
        Label avgscore = new Label("Average score: " + Filework.getAvgscore(stats, gamename));
        addObject(avgscore, 5,5);
        //switchWorld(this);
        Button b = new Button() {

            @Override
            public void tick() {

            }

            @Override
            public void clickEvent(MouseEventInfo e) {
                Deathscreen w= (Deathscreen) world;
                Snakeworld s = (Snakeworld) w.origin;
                switchWorld(new Snakeworld(Snakeworld.g, s.stats,s.ui.backgroundImage, s.bg_opaqueness));
            }
        };
        b.setText("Retry");
        b.setSize(200,100);
        addObject(b, 2,7);
        b = new Button() {
            @Override
            public void clickEvent(MouseEventInfo e) {
                World.switchWorld(new Settingsscreen());
            }

            @Override
            public void tick() {

            }
        };
        b.setText("Back to settings");
        b.setSize(200, 100);
        addObject(b,5,7 );

        b = new Button() {
            @Override
            public void clickEvent(MouseEventInfo e) {
                Filework.writeScores(stats);
                world.mainframe.dispose();
            }

            @Override
            public void tick() {

            }
        };
        b.setText("Exit");
        b.setSize(200,100);
        addObject(b, 7, 7);
        Filework.writeScores(stats);
    }

    @Override
    public void tick() {

    }

    public void keyTyped(int key) {
        //switchWorld(origin);
    }

    public void windowClosed(WindowEvent e) {
        System.out.println("saving stats");
        Filework.writeScores(stats);
    }
}
