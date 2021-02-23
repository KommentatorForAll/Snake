package Enginegames;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Utils {

    /**
     * loads an image from anywhere on your pc.
     * if no extention is given, .png is added automaticly
     * @param filelocation the location of the image
     * @return the loaded image. !!may be null if no image was found!!
     */
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

    /**
     * loads an image from the assets folder, located at the src root.
     * @param filename name of the image
     * @return the loaded image !!may be null if no image was found!!
     */
    public static AdvancedImage loadImageFromAssets(String filename) {
        return loadImage(System.getProperty("user.dir")+"/assets/" +filename);
    }

    /**
     * returns the dimension of the given string.
     * @param font the font of the text
     * @param text the text itself
     * @return the dimension of the string, when drawn as {width, height}
     */
    public static int[] getStringDimensions(java.awt.Font font, String text) {
        Graphics2D graphics = (Utils.loadImageFromAssets("Invis.png")).createGraphics();
        if (graphics == null)
        {
            return new int[] {0,0};
        }

        graphics.setFont(font);

        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        String[] lines = text.split("[\n\r]");
        FontMetrics fontMetrics = graphics.getFontMetrics(font);
        int height = fontMetrics.getHeight();
        int width = Arrays.stream(lines).map(fontMetrics::stringWidth).max((a, b) -> a - b).orElse(0);
        return new int[] {width, height};
    }

    /**
     * Bc T is just pain in the ass
     * @param T Testosterone
     * @return yes.
     */
    public <T> int pain(ArrayList<T> T) {
        return Integer.MAX_VALUE;
    }
}
