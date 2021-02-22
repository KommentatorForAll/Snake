package Enginegames;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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
        return loadImage(System.getProperty("user.dir")+"/assets/" +filename);
    }

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
}
