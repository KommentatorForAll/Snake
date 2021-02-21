package Enginegames;

import java.awt.*;
import java.util.*;

public class Button extends WorldObj {
    public String name, text;
    public java.awt.Font font;
    public java.awt.Color textColor = Color.WHITE, backgroundColor = Color.BLACK, bgColorHover = Color.BLACK;
    public int width, height;
    public boolean dynamic;
    public AdvancedImage defaultImage;


    public Button() {
        this("","",400,100);
    }

    public Button(String name, String text, int width, int height) {
        this(name, text, width, height, Color.WHITE, Font.getFont(Font.SERIF));
    }

    public Button(String name, String text, int width, int height, Color textColor, Font font, boolean dynamic) {
        this.textColor = textColor;
        this.font = font;
        this.name = name;
        this.text = text;
        this.width = width;
        this.height = height;
        this.dynamic = dynamic;
        defaultImage = new AdvancedImage(width, height);
        if (!text.isBlank()) {
            updateText(text);
        }
    }

    public Button(String name, String text, java.awt.Font font, java.awt.Color color) {
        this(name, text, 400, 100, color, font);
    }

    public Button(String name, String text, int width, int height, Color color, Font font) {
        this(name, text, width, height, color, font, false);
    }

    public void tick() {

    }

    public void updateText(String text) {
        updateText(text, defaultImage);
    }

    public void updateText(String text, AdvancedImage img) {
        this.text = text;
        AdvancedImage textImg = img.clone();
        textImg.drawText(textColor, font, text, img.getWidth()/2, img.getHeight()/2);
        setImage(textImg);
    }
}