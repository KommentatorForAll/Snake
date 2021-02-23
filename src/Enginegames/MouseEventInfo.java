package Enginegames;

import java.awt.event.MouseEvent;

/**
 * Wrapper class, to contain both, the event type of a mouseevent, as well as the event itself.
 */
public class MouseEventInfo {
    public static final int MOUSE_PRESSED = 0,
            MOUSE_CLICKED = 1,
            MOUSE_RELEASED = 2,
            MOUSE_ENTERED = 3,
            MOUSE_EXITED = 4;
    public int type;
    public MouseEvent e;
    public MouseEventInfo(MouseEvent e, int type) {
        this.e = e;
        this.type = type;
    }
}
