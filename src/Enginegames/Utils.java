package Enginegames;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Utils {

    public static AdvancedImage loadImage(String filelocation) {
        if (!filelocation.matches("\\..+$")) {
            filelocation += ".png";
        }
        try {
            return new AdvancedImage(ImageIO.read(new File(filelocation)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AdvancedImage loadImageFromAssets(String filename) {
        return loadImage(System.getProperty("user.dir")+"\\assets\\" +filename);
    }
}
