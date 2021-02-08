package Enginegames;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorldUI extends JPanel {

    public BufferedImage backgroundImage;
    public ImageOption bgOption = ImageOption.NONE;
    public Canvas canvas;
    public Graphics g;

    public Map<WorldObj, int[]> objs;

    public WorldUI(String name, int width, int height) {
        this(name, width, height, ImageOption.NONE);
    }

    public WorldUI(String name, int width, int height, ImageOption bgOption) {
        super();
        canvas = new Canvas();
        g = canvas.getGraphics();
        objs = new HashMap<>();
        this.bgOption = bgOption;
        canvas.setSize(width, height);
        setSize(width, height);
        add(canvas);
        setVisible(true);
    }

    public void setBackground(BufferedImage img) {
        backgroundImage = img;
        redraw();
    }

    public void redraw() {
        if (backgroundImage != null)
            switch(bgOption) {
                case TILED:
                    for (int x = 0; x < getWidth(); x+= backgroundImage.getWidth(null)) {
                        for (int y = 0; y < getHeight(); y+= backgroundImage.getHeight(null)) {
                            g.drawImage(backgroundImage, x,y,null);
                        }
                    }
                    break;

                case STRETCHED:
                    int hscale = getHeight()/backgroundImage.getHeight(null);
                    int wscale = getWidth()/backgroundImage.getWidth(null);
                    BufferedImage after = new BufferedImage(getWidth(), getHeight(), backgroundImage.getType());
                    AffineTransform scaleInstance = AffineTransform.getScaleInstance(wscale, hscale);
                    AffineTransformOp scaleOp = new AffineTransformOp(scaleInstance, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                    scaleOp.filter(backgroundImage, after);
                    g.drawImage(after, 0,0,null);

                case NONE:
                default:
                    g.drawImage(backgroundImage, 0,0,null);
                    break;
            }
        objs.forEach((obj, pos) -> g.drawImage(obj.img, pos[0], pos[1], null));
        g.dispose();
    }

    public void addImage(WorldObj obj, int[] pos) {
        objs.put(obj, pos);
    }

    public enum ImageOption {
        STRETCHED,
        TILED,
        NONE
    }
}
