package Enginegames;

import java.awt.*;

public class Dialog extends World {

    public static Font font = new Font(Font.SERIF, Font.PLAIN, 20);
    public static Color textColor;

    private boolean gotResult;
    private Object result;

    private Dialog(String dialog, String initValue, int type) {
        super(10,5,64);
        AdvancedImage img = new AdvancedImage(640,320);
        img.fill(Color.BLACK);
        setBackground(img);
        Label l = new Label(dialog);
        l.setFont(font);
        l.setBackgroundColor(Color.BLACK);
        l.setBorderColor(Color.BLACK);
        l.setTextColor(Color.WHITE);
        addObject(l, 5,2);
        Textfield tf = new Textfield();
        tf.setSize(128,64);
        tf.setFont(font);
        tf.setText(initValue);
        switch(type) {
            case 1:
                tf.isNumeric = true;
            case 0:
                addObject(tf, 5,4);
                Button apply = new Button() {
                    @Override
                    public void clickEvent(MouseEventInfo e) {
                        result = tf.text;
                        gotResult = true;
                    }

                    @Override
                    public void tick() {

                    }
                };
                apply.setText("apply");
                apply.setSize(64,32);
                apply.setFont(font);
                addObject(apply, 3,4);
                break;

            case 2:
                Button confirm = new Button() {
                    @Override
                    public void clickEvent(MouseEventInfo e) {
                        result = true;
                        gotResult = true;
                    }

                    @Override
                    public void tick() {

                    }
                }, abord = new Button() {
                    @Override
                    public void clickEvent(MouseEventInfo e) {
                        result = false;
                        gotResult = true;
                    }

                    @Override
                    public void tick() {

                    }
                };
                confirm.setText("Yes");
                confirm.setFont(font);
                confirm.setSize(64,32);
                abord.setText("No");
                abord.setFont(font);
                abord.setSize(64,32);
                addObject(confirm, 3,4);
                addObject(abord, 7,4);
                break;
        }
    }

    @Override
    public void tick() {

    }

    public static Object ask(String dialog, String initValue, int type) {
        Dialog d = new Dialog(dialog, initValue, type);
        World origin = switchWorld(d).get(0);
        while (!d.gotResult) {
            try {
                Thread.sleep((long)(1000/e.tps));
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        switchWorld(origin);
        return d.result;
    }

    public static String askString(String dialog, String... initValue) {
        return (String)ask(dialog, initValue.length == 0?"" : initValue[0], 0);
    }

    public static boolean askConfirm(String dialog, String... initValue) {
        return (boolean) ask(dialog, initValue.length == 0?"":initValue[0], 2);
    }

    public static int askNumber(String dialog, String... initValue) {
        return (int)ask(dialog, initValue.length == 0?"" : initValue[0], 1);
    }
}
