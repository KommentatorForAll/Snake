package Enginegames;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

public class AdvancedImage extends BufferedImage implements Cloneable {

    public ImagePosition imgpos = ImagePosition.CENTER;
    public ImageSizing imgs = ImageSizing.NONE;

    public AdvancedImage(int width, int height) {
        super(width, height, TYPE_INT_ARGB);
    }

    public AdvancedImage(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    public AdvancedImage(BufferedImage img) {
        super(img.getWidth(), img.getHeight(), img.getType());
        drawImage(img);
    }

    public void drawImage(BufferedImage img) {
        drawImage(img, 0,0);
    }

    public void drawImage(BufferedImage img, int x, int y) {
        drawImage(img, x, y, 1);
    }

    private void drawImage(BufferedImage img, int x, int y, float opaque) {
        drawImage_(img, x, y, opaque);
    }

    public void drawImage_(BufferedImage image, int x, int y, float opaque) {
        Graphics2D g2d = createGraphics();
        g2d.setComposite(
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opaque));
        g2d.drawImage(image, x, y, null);
        g2d.dispose();
    }

    public void drawImage(AdvancedImage img, int x, int y, float opaque) {
        int mw = getWidth(), mh = getHeight(), iw = img.getWidth(), ih = img.getHeight(), wx = x, wy = y;
        AdvancedImage i;
        switch (img.imgs) {
            case TILE:
                i = new AdvancedImage(img);
                while (wy < mh) {
                    while (wx < mw) {
                        i.drawImage((BufferedImage) img, wx, wy, opaque);
                        wx += iw;
                    }
                    wx = x;
                    wy += ih;
                }
                break;
            case STRETCH:
                i = img.scale(mw-x, mw-y);
                break;

            case NONE:
            default:
                i = new AdvancedImage(img);
                break;
        }
        switch (img.imgpos) {
            case TOP_LEFT:
                drawImage_((BufferedImage) i, x, y, opaque);
                break;

            case CENTER:
                drawImage_((BufferedImage) i, x-i.getWidth()/2, y-i.getHeight()/2, opaque);
        }
    }

    public void drawString(String str, int x, int y) {

    }

    public AdvancedImage scale(int width, int height) {
//        if (width != getWidth() || height != getHeight()) {
//            Graphics2D g = createGraphics();
//            g.setComposite(AlphaComposite.Src);
//            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
//            g.drawImage(this, 0, 0, width, height, (ImageObserver)null);
//            g.dispose();
//        }
        int hscale = width/getHeight(null);
        int wscale = height/getWidth(null);
        AdvancedImage after = new AdvancedImage(width, height, getType());
        AffineTransform scaleInstance = AffineTransform.getScaleInstance(wscale, hscale);
        AffineTransformOp scaleOp = new AffineTransformOp(scaleInstance, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        scaleOp.filter(this, after);
        return after;
    }

    public AdvancedImage clone() {
        BufferedImage img = deepCopy(this);
        AdvancedImage i = new AdvancedImage(img);
        i.imgpos = imgpos;
        i.imgs = imgs;
        return i;
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public String toString() {
        return super.toString() + " of Dimension: " + getWidth() + "*" + getHeight();
    }

    public AdvancedImage rotate(double angle) {
        return rotateImageByDegrees(this, angle);
    }

    public static AdvancedImage rotateImageByDegrees(AdvancedImage img, double angle) {
        angle = -angle;
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2.0, (newHeight - h) / 2.0);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        AdvancedImage i = new AdvancedImage(rotated);
        i.imgpos = img.imgpos;
        i.imgs = img.imgs;

        return i;
    }

    public AdvancedImage replaceColor(Color oldColor, Color newColor) {
        return replaceColor(this, oldColor, newColor);
    }

    public AdvancedImage replaceColor(int oldColor, int newColor) {
        return replaceColor(this, oldColor, newColor);
    }

    public static AdvancedImage replaceColor(AdvancedImage image, Color target, Color preferred) {
        return replaceColor(image, target.getRGB(), preferred.getRGB());
    }

    public static AdvancedImage replaceColor(AdvancedImage image, int target, int preferred) {
        int width = image.getWidth();
        int height = image.getHeight();
        AdvancedImage newImage = new AdvancedImage(width, height, image.getType());
        int color;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                color = image.getRGB(i, j);
                if (color == target) {
                    newImage.setRGB(i, j, preferred);
                }
                else {
                    newImage.setRGB(i, j, color);
                }
            }
        }

        return newImage;
    }

    public void drawText(Color color, Font font, String text, int x, int y) {
        drawText(color, font, text, x, y, VerticalAlignment.CENTER, HorizontalAlignment.CENTER);
    }

    public void drawText(Color color, Font font, String text, int x, int y, VerticalAlignment verticalAlignment, HorizontalAlignment horizontalAlignment)
    {
        Graphics2D graphics = createGraphics();
        if (graphics == null)
        {
            System.err.println("No valid graphics");
            return;
        }

        graphics.setColor(color);
        graphics.setFont(font);

        if (x < 0 || x >= getWidth())
        {
            x = getWidth() / 2;
        }
        if (y < 0 || y >= getHeight())
        {
            y = getHeight() / 2;
        }

        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        String[] lines = text.split("[\n\r]");
        FontMetrics fontMetrics = graphics.getFontMetrics(font);
        int height = fontMetrics.getHeight();
        if(verticalAlignment == VerticalAlignment.CENTER)
        {
            y -= (height * (lines.length / 2));
        }
        if (verticalAlignment == VerticalAlignment.BOTTOM)
        {
            y -= height * lines.length;
        }
        y += (height / 4);
        for (int i = 0; i < lines.length; i++)
        {
            int modX = x;
            if(horizontalAlignment == HorizontalAlignment.CENTER)
            {
                modX = x - (fontMetrics.stringWidth(lines[i]) / 2);
            }
            if(horizontalAlignment == HorizontalAlignment.RIGHT)
            {
                modX = x - fontMetrics.stringWidth(lines[i]);
            }
            graphics.drawString(lines[i], modX, y + i * height);
        }

        graphics.dispose();
    }

    public enum ImagePosition {
        TOP_LEFT,
        CENTER
    }

    public enum ImageSizing {
        NONE,
        TILE,
        STRETCH
    }

    enum VerticalAlignment
    {
        TOP,
        CENTER,
        BOTTOM
    }

    enum HorizontalAlignment
    {
        LEFT,
        CENTER,
        RIGHT
    }
}
