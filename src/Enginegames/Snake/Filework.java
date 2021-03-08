package Enginegames.Snake;

import Enginegames.AdvancedImage;
import Enginegames.Utils;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;

public class Filework {

    public static String sfl = Utils.fetchAppdataFolder()+ "Spielebox/Snake/";
    public static File sprites = Utils.extract("assets/sprites", sfl);


    public static Tag readScores() {
        try {
            return Tag.readFrom(new FileInputStream(sfl+"scores.nbt"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Creating new saves, as none existed");
            Tag t = createDefaultSave();
            writeScores(t);
            return t;
        }
    }

    public static void writeScores(Tag t) {
        try {
            System.out.println(sfl+"scores.nbt");
            File f = new File(sfl+"scores.nbt");
            f.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(f);
            t.writeTo(fos);
            fos.close();

        } catch (IOException e) {
            System.err.println("Error while writing saves");
            e.printStackTrace();
        }
    }

    public static Tag newGameInstance(String gamename, int highscore) {
        //E, bc E is a lot better than T
        Tag[] e = new Tag[4];
        e[0]=new Tag(Tag.Type.TAG_String, "playername", "");
        e[1]=new Tag(Tag.Type.TAG_Int, "highscore", highscore);
        e[2]=new Tag(Tag.Type.TAG_Int, "gamecount", 1);
        e[3]=new Tag(Tag.Type.TAG_Double, "avgscore", highscore*01.0);
        return new Tag(Tag.Type.TAG_Compound, gamename, e);
    }

    public static void addToStats(Tag stats, String gamename, int highscore) {
        stats.addTag(newGameInstance(gamename, highscore));
    }

    public static Tag createDefaultSave() {
        return new Tag(Tag.Type.TAG_Compound, "snake", new Tag[] {new Tag(Tag.Type.TAG_End, null, null)} );
    }

    public static boolean checkHighscore(Tag stats, String gamename, int score) {
        Tag game = stats.findNextTag(gamename);
        if (game == null) {
            game = newGameInstance(gamename, score);
            stats.addTag(game);
            return true;
        }

        Tag highscore = game.findNextTag("highscore"),
                  avg = game.findNextTag("avgscore"),
                  cnt = game.findNextTag("gamecount");

        int games = (int) cnt.getValue();
        double avgscore = (double) avg.getValue();
        avg.setValue(((avgscore*games)+score)/(games+1));
        cnt.setValue(games+1);
        System.out.println("checking hs");
        game.print();
        stats.print();

        return (score > (int)highscore.getValue());
    }

    public static void setHighscore(int score, Tag stats, String gamename, String playername) {
        Tag game = stats.findNextTag(gamename);
        Tag highscore = game.findNextTag("highscore");
        highscore.setValue(score);
        game.findNextTag("playername").setValue(playername);

    }


    public static double getAvgscore(Tag scores, String gamename) {
        return (double) scores.findNextTag(gamename).get("avgscore");
    }

    public static int getHighscore(Tag stats, String gamename) {
        return (int) stats.findNextTag(gamename).get("highscore");
    }

    public static Map<String, Map<String, AdvancedImage>> loadAllSprites() {
        Map<String, Map<String, AdvancedImage>> sprites = new TreeMap<>();
        Map<String, AdvancedImage> tmp;
        Filework.sprites = new File(Utils.assets+"/sprites");
        for (File f : Filework.sprites.listFiles()) {
            if (!f.isDirectory()) continue;
            boolean fn = f.getName().equals("apple");
            tmp = new TreeMap<>();
            for(File img : f.listFiles()) {
                String name = img.getName();
                if ((!name.endsWith("_invis.png")) && !fn) continue;
                try {
                    tmp.put(name.replace("\\.png", ""), new AdvancedImage(ImageIO.read(img.getAbsoluteFile())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            sprites.put(f.getName(), tmp);
        }
        return sprites;
    }

    public static ArrayList<Snakeworld.Gamemode> loadGamemodes() {
        ArrayList<Snakeworld.Gamemode> gms = new ArrayList<>();

        String f = Utils.readFromAssets("others/Gamemodes.gms");
        String[] split;
        for (String line : f.split("\n")) {
            split = line.split(",");
            gms.add(new Snakeworld.Gamemode(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]), Double.parseDouble(split[5]), Double.parseDouble(split[6])));
        }
        gms.add(Snakeworld.Gamemode.CUSTOM);
        gms.add(Snakeworld.Gamemode.DEFAULT);

        return gms;
    }
}
