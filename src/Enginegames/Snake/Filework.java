package Enginegames.Snake;

import Enginegames.Utils;

import java.io.*;

public class Filework {

    public static String sfl = Utils.fetchAppdataFolder()+ "Spielebox/Snake/";

    public static Tag readScores() {
        try {
            return Tag.readFrom(new FileInputStream(sfl+"scores.nbt"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Creating new saves, as none existed");
            return createDefaultSave();
        }
    }

    public static void writeScores(Tag t) {
        try {
            System.out.println(sfl+"scores.nbt");
            FileOutputStream fos = new FileOutputStream(sfl+"scores.nbt");
            t.writeTo(fos);
            fos.close();

        } catch (IOException e) {
            System.err.println("Error while writing saves");
            e.printStackTrace();
        }
    }

    public static Tag newGameInstance(String gamename, int highscore, String playername) {
        //E, bc E is a lot better than T
        Tag[] e = new Tag[4];
        e[0]=new Tag(Tag.Type.TAG_String, "playername", playername);
        e[1]=new Tag(Tag.Type.TAG_Int, "highscore", highscore);
        e[2]=new Tag(Tag.Type.TAG_Int, "gamecount", 1);
        e[3]=new Tag(Tag.Type.TAG_Double, "avgscore", highscore*01.0);
        return new Tag(Tag.Type.TAG_Compound, gamename, e);
    }

    public static void addToStats(Tag stats, String gamename, int highscore, String playername) {
        stats.addTag(newGameInstance(gamename, highscore, playername));
    }

    public static Tag createDefaultSave() {
        return new Tag(Tag.Type.TAG_Compound, "snake", new Tag[] {new Tag(Tag.Type.TAG_End, null, null)} );
    }

    public static boolean checkHighscore(Tag stats, String gamename, int score, String playername) {
        Tag game = stats.findNextTag(gamename);
        if (game == null) {
            game = newGameInstance(gamename, score, playername);
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

        if (score > (int)highscore.getValue()) {
            highscore.setValue(score);
            game.findNextTag("playername").setValue(playername);
            return true;
        }
        return false;
    }


    public static double getAvgscore(Tag scores, String gamename) {
        return (double) scores.findNextTag(gamename).get("avgscore");
    }

    public static int getHighscore(Tag stats, String gamename) {
        return (int) stats.findNextTag(gamename).get("highscore");
    }
}
