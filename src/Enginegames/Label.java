package Enginegames;

/**
 * an empty button, made just to show some ~~pride~~ text
 */
public class Label extends Button{

    public Label() {
        backgroundHoverColor = backgroundColor;
    }

    public Label(String text) {
        this();
        setText(text);
    }

    public void tick() {}

    public void clickEvent(MouseEventInfo e) {}
}
