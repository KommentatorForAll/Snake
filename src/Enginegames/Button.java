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
        text = "";
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
        setImage(img);
    }

    public void mouseEvent(MouseEventInfo e) {
        if (Main.enableDebug)
            System.out.println("clicked on button " + toString());
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

    public final void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        update();
    }

    public final void setBorderWidth(int width) {
        borderWidth = width;
        update();
    }

    public final void setFont(Font font) {
        this.font = font;
        update();
    }

    public final void setText(String text) {
        this.text = text;
        update();
    }

    public final void setTextColor(Color textColor) {
        this.textColor = textColor;
        update();
    }

    public final void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        update();
    }

    public final void setBackgroundHoverColor(Color backgroundHoverColor) {
        this.backgroundHoverColor = backgroundHoverColor;
        update();
    }

    public final void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        update();
    }

    public final void setBackgroundImage(AdvancedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
        update();
    }

    public abstract void clickEvent();
}