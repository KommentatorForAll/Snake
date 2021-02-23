package Enginegames;

import java.awt.*;
import java.util.*;

public abstract class Button extends WorldObj {

    public String text;
    public int width, height, borderWidth;
    public Color textColor = new Color(0,0,0,255), backgroundColor = new Color(255,255,255,255), backgroundHoverColor = Color.LIGHT_GRAY, borderColor = new Color(0,0,0,255);
    public Font font;
    public AdvancedImage backgroundImage;

    public Button() {
        text = "test";
        width = 400;
        height = 100;
        font = new Font(Font.SERIF, Font.PLAIN, 20);
        borderWidth = 5;
        update();
    }

    public void update() {
        AdvancedImage img = new AdvancedImage(width, height);
        img.fill(borderColor);
        img.drawRect(borderWidth, borderWidth, width-(borderWidth*2), height-(borderWidth*2), backgroundColor);
        if (backgroundImage != null) img.drawImage(backgroundImage);
        if (!text.isEmpty()) img.drawText(textColor, font, text);
        System.out.println(img);
        setImage(img);
    }

    public void mouseEvent(MouseEventInfo e) {
        //if (Main.enableDebug)
        System.out.println("called event");
        if (e.type == MouseEventInfo.MOUSE_PRESSED) {
            Color tmp = backgroundColor;
            backgroundColor = backgroundHoverColor;
            backgroundHoverColor = tmp;
            update();
        }

        else if (e.type == MouseEventInfo.MOUSE_CLICKED) {
            clickEvent();
        }

        else if (e.type == MouseEventInfo.MOUSE_RELEASED) {
            Color tmp = backgroundColor;
            backgroundColor = backgroundHoverColor;
            backgroundHoverColor = tmp;
            update();
        }
    }

    public abstract void clickEvent();
}