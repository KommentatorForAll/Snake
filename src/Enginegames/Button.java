package Enginegames;

import java.awt.*;
import java.util.*;

public abstract class Button extends WorldObj {

    public String text;
    public int width, height, borderWidth;
    public Color textColor = new Color(0,0,0,255), backgroundColor = new Color(255,255,255,255), backgroundHoverColor = Color.LIGHT_GRAY, borderColor = new Color(0,0,0,255);
    public Font font;
    public AdvancedImage backgroundImage;
    public boolean enabled = true;

    public Button() {
        text = "";
        width = 400;
        height = 100;
        font = new Font(Font.SERIF, Font.PLAIN, 20);
        borderWidth = 5;
        update();
    }

    /**
     * updates the displayed button
     */
    public final void update() {
        AdvancedImage img = new AdvancedImage(width, height);
        img.fill(borderColor);
        img.drawRect(borderWidth, borderWidth, width-(borderWidth*2), height-(borderWidth*2), backgroundColor);
        if (backgroundImage != null) img.drawImage(backgroundImage);
        if (!text.isEmpty()) img.drawText(textColor, font, text);
        setImage(img);
    }

    /**
     * called when the button is clicked.
     * @param e the event created by the click
     */
    public final void _mouseEvent(MouseEventInfo e) {
        if (Main.enableDebug)
            System.out.println("clicked on button " + toString());
        if (!enabled) return;
        if (e.type == MouseEventInfo.MOUSE_PRESSED) {
            Color tmp = backgroundColor;
            backgroundColor = backgroundHoverColor;
            backgroundHoverColor = tmp;
            update();
        }

        else if (e.type == MouseEventInfo.MOUSE_CLICKED) {
            clickEvent(e);
        }

        else if (e.type == MouseEventInfo.MOUSE_RELEASED) {
            Color tmp = backgroundColor;
            backgroundColor = backgroundHoverColor;
            backgroundHoverColor = tmp;
            update();
        }
        mouseEvent(e);
    }

    /**
     * Called once the button is clicked.
     * @param e the click event information
     */
    public abstract void clickEvent(MouseEventInfo e);

    /**
     * updates the size of the button
     * @param width the new width of the button in pixel
     * @param height the new height of the button in pixel
     */
    public final void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        update();
    }

    /**
     * sets the new border width, around the buttons background. set to 0 for no border
     * @param width the new width in pixel
     */
    public final void setBorderWidth(int width) {
        borderWidth = width;
        update();
    }

    /**
     * sets the font, which is used to write on the button
     * @param font the new font
     */
    public final void setFont(Font font) {
        this.font = font;
        update();
    }

    /**
     * sets the currently displayed text of the button
     * @param text the text to be displayed
     */
    public final void setText(String text) {
        this.text = text;
        update();
    }

    /**
     * updates the text color to the specified color
     * @param textColor the color, the text should shine in
     */
    public final void setTextColor(Color textColor) {
        this.textColor = textColor;
        update();
    }

    /**
     * sets the background color of the image
     * @param backgroundColor the color the background of the button should be
     */
    public final void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        update();
    }

    /**
     * the color, the background is in, when clicked and hold
     * @param backgroundHoverColor the new color
     */
    public final void setBackgroundHoverColor(Color backgroundHoverColor) {
        this.backgroundHoverColor = backgroundHoverColor;
        update();
    }

    /**
     * sets the color, surrounding the button
     * @param borderColor the new color
     */
    public final void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        update();
    }

    /**
     * sets the image the button has
     * @param backgroundImage the image displayed on the button
     */
    public final void setBackgroundImage(AdvancedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
        update();
    }

}