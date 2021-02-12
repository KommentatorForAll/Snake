package Enginegames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class WorldUI extends JPanel {

    public BufferedImage backgroundImage;
    public ImageOption bgOption;

    public HashMap<WorldObj, int[]> objs;
    public int pxsize;

    public WorldUI(int width, int height) {
        this(width, height, ImageOption.NONE,1, null);
    }

    public WorldUI(int width, int height, ImageOption bgOption, int pxsize, KeyListener kl) {
        super();
        objs = new HashMap<>();
        this.bgOption = bgOption;
        setSize(width, height);
        setPreferredSize(getSize());
        setMaximumSize(getSize());
        setVisible(true);
        this.pxsize = pxsize;
        addKeyListener(kl);
    }

    public void setBackground(BufferedImage img) {
        backgroundImage = img;
        repaint();
    }

    public void paint(ArrayList<WorldObj> objects) {
        objs = new HashMap<>();
        objects.forEach((o) -> objs.put(o, new int[] {o.x,o.y}));
        repaint();
    }

    public void paintComponent(Graphics g) {
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
        objs.forEach((obj, pos) -> g.drawImage(obj.img, pos[0]*pxsize, pos[1]*pxsize, null));
    }

    public enum ImageOption {
        STRETCHED,
        TILED,
        NONE
    }
}
