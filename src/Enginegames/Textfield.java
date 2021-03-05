package Enginegames;

import java.awt.*;

public class Textfield extends Button {

    /**
     * maximum length of the included textfield.
     * -1 for no length limit
     */
    public int maxsize = -1;
    public boolean isNumeric, isSelected;
    public Color selectedBorderColor;


    @Override
    public final void clickEvent(MouseEventInfo e) {
        world.objectsOf(Textfield.class).forEach(Textfield::deselect);
        isSelected = true;
        Color tmp = borderColor;
        setBorderColor(selectedBorderColor);
        selectedBorderColor = tmp;
    }

    @Override
    public void tick() {

    }

    public final void setSelectedBorderColor(Color clr) {
        if (isSelected) {
            setBorderColor(clr);
        }
        else {
            selectedBorderColor = clr;
        }
    }

    public final void deselect() {
        if (isSelected) {
            isSelected = false;
            Color tmp = borderColor;
            setBorderColor(selectedBorderColor);
            selectedBorderColor = tmp;
        }
    }

    public final void keyTyped(int c) {
        if ((""+ (char) c).matches("\\W")) {
            if (c == 8) {
                if (!text.isEmpty()) {
                    setText(text.substring(0, text.length()-1));
                }
                if (isNumeric && text.isEmpty()) {
                    setText("0");
                }
            }
            return;
        }

        if (isNumeric) {
            if (!(((char) c)+"").matches("\\d")) {
                return;
            }
        }
        if (text.isEmpty()) {
            if ((((char) c)+"").matches("\\s")) {
                return;
            }
        }
        if (isNumeric && text.equals("0")) {
            text = (char) c+"";
        }
        else
            text+=(char)c;
        if (maxsize != -1) {
            if (text.length() > maxsize) {
                text = text.substring(0, maxsize);
            }
        }
        update();
    }
}
