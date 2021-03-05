package Enginegames;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static String assets = System.getProperty("user.dir")+"/assets/";

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
        return loadImage(assets+"sprites/" +filename);
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

    /**
     * fetches the OS the game is run on
     * @return the OS currently booted
     */
    public static String getOS() {
        String os = System.getProperty("os.name");
        System.out.println(os);
        if (os.contains("Windows")) return "windows";
        if (os.contains("Mac")) return "mac";
        if (os.contains("Linux")) return "linux";
        return os;
    }

    public static String fetchAppdataFolder() {
        return fetchAppdataFolder(getOS());
    }

    /**
     * Fetches the appdata folder from the given os
     * @param os the operating system, the program is running on
     * @return the path for appdata
     */
    public static String fetchAppdataFolder(String os) {
        String dir = System.getenv("APPDATA");
        if (dir != null)
            return dir+"/";
        switch (os) {
            case "mac":
                return " ~/Library/Application Support/";//"bad os, we don't support such crap";
            case "linux":
                return "~/.config/";
            default:
                return "./";
        }
    }

    public static Font loadFontFromAssets(String name) {
        // Creates an awt font from the file as true type font (.ttf)
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(assets+"fonts/"+name));
            //generates an graphics environment for idk what, not my script, but scisneromams i'm just commenting this crap
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //registers the font in the graphics environment
            graphicsEnvironment.registerFont(font);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return font;
    }

    /**
     * Loads all fonts from the assets folder as java.awt.Font
     * This is needed as Greenfoot sucks and one requies a huge script using awt fonts to center some text
     * @return an hashmap of fonts as name -> font
     */
    public static HashMap<String, Font> loadAllFonts() {
        HashMap<String, java.awt.Font> fonts = new HashMap<>();
        File dir = new File(assets+"fonts/");
        File[] dirFiles = dir.listFiles();
        String name;
        if (dirFiles != null) {
            for (File child : dirFiles) {
                name = child.getName();
                if (!name.endsWith(".ttf")) continue;
                try {
                    fonts.put(name.replaceAll("\\.\\w+$", ""), loadFontFromAssets(name));
                }
                catch (Exception e) {
                    System.err.println("Error while loading font {}".replace("{}", name));
                    e.printStackTrace();
                }
            }
        }
        return fonts;
    }

    public static void createImage(File output, int size) throws IOException
    {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < size * size; i++)
        {
            image.setRGB(i % size, i / size, 0x00FF0000);
        }

        if (!ImageIO.write(image, "png", output))
        {
            System.err.println("No writer found!");
        }
    }


    public static void readImage(File file) throws IOException
    {
        BufferedImage image = ImageIO.read(file);
        for (int y = 0; y < image.getHeight(); y++)
        {
            for (int x = 0; x < image.getWidth(); x++)
            {
                System.out.printf(" %s ", Integer.toHexString(image.getRGB(x, y)));
            }
            System.out.println();
        }
    }

    public static Object getKey(Map m, Object value) {
        for (Object x : m.keySet()) {
            if (m.get(x).equals(value)) return x;
        }
        return null;
    }
}
