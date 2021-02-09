package Enginegames;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Util {

    public static BufferedImage loadImage(String filelocation) {
        if (!filelocation.matches("\\..+$")) {
            filelocation += ".png";
        }
        System.out.println(filelocation);
        try {
            return ImageIO.read(new File(filelocation));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage loadImageFromAssets(String filename) {
        return loadImage(System.getProperty("user.dir")+"\\assets\\" +filename);
    }
}
