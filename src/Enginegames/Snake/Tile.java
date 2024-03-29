package Enginegames.Snake;

import Enginegames.*;

import java.awt.image.BufferedImage;

public class Tile extends WorldObj {

    public static AdvancedImage line ;//= AdvancedImage.rotateImageByDegrees(Utils.loadImageFromAssets("mid/snake_body"),90);
    public static AdvancedImage corner ;//= Utils.loadImageFromAssets("corner/snake_corner");
    public static AdvancedImage tail ;//= AdvancedImage.rotateImageByDegrees(Utils.loadImageFromAssets("tail/snake_tail"),-90);


    public Head head;
    public int age;
    public int dir;


    public Tile(Head head, int dir) {
        this(head, dir, 0);
    }

    public Tile(Head head, int dir, int age) {
        super();
        this.head = head;
        this.dir = dir;
        this.age = age;
        selectImage();
        if (img != null)
        img.imgs = AdvancedImage.ImageSizing.STRETCH;
        //setImage(Util.loadImageFromAssets("python"));
    }

    private void selectImage() {
        int back = dir/4;
        int front = dir%4;
        int workdir;
        AdvancedImage selected = line;
        if (back%2!=front%2) {
            selected = corner;
            workdir = (front+1)%4 == back? front:back+2;
            selected = AdvancedImage.rotateImageByDegrees(selected, workdir*90);
        }
        else {
            if (back%2==1) {
                selected = AdvancedImage.rotateImageByDegrees(selected, 90);
            }
        }
        setImage(selected);
    }

    @Override
    public void tick() {
        if (!((Snakeworld)world).running) return;
        age++;
        if (age >= head.size) {
            world.removeObject(this);
        }
        if (age == head.size-1) {
            setImage(AdvancedImage.rotateImageByDegrees(tail, (dir%4)*90+180));
            img.imgs = AdvancedImage.ImageSizing.STRETCH;
        }
    }
}
