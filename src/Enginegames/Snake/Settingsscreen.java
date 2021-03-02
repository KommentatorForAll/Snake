package Enginegames.Snake;

import Enginegames.*;
import Enginegames.Button;
import Enginegames.Label;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Settingsscreen extends World {

    Textfield startsize, width, height, pxsize, tps;
    ScrollButton primaryColor, secondaryColor, tertiaryColor, gamemode;
    Label pcl, scl, tcl, gml, szl, wl, hl, pxl, tpsl, title;
    Button start, exit;

    public Settingsscreen() {
        super(10,15,64);
        AdvancedImage img = new AdvancedImage(64,64);
        img.fill(Color.BLACK);
        setBackground(img);
        setBackgroundOption(WorldUI.ImageOption.STRETCHED);
        List<AdvancedImage> clrs = colorOptions();

        title = new Label("settings") {
            public void tick() {
                setTextColor(new Color(clrs.get(random.nextInt(clrs.size())).getRGB(0,0)));
            }
        };
        title.setFont(Snakeworld.fonts.get("pixel-bubble").deriveFont(40F));
        title.setTextColor(new Color(clrs.get(random.nextInt(clrs.size())).getRGB(0,0)));
        title.setBackgroundColor(Color.BLACK);
        title.setBackgroundHoverColor(Color.BLACK);
        addObject(title, 5,1);

        List<String> gmimgs = Arrays.stream(Snakeworld.Gamemode.values).map(g -> g.name).map(String::toLowerCase).collect(Collectors.toList());
        gamemode = new ScrollButton(gmimgs, gmimgs.stream().filter(g -> Snakeworld.g.name.equals(g)).findFirst().orElse("default")){
            public void clickEvent(MouseEventInfo e) {
                super.clickEvent(e);
                Settingsscreen s = (Settingsscreen) world;
                Snakeworld.Gamemode g = Snakeworld.Gamemode.from((String) selected);
                s.startsize.setText(g.start_size+"");
                s.width.setText(g.width+"");
                s.height.setText(g.height+"");
                s.pxsize.setText(g.pxsize+"");

                boolean x = g.name.equals("custom");
                s.startsize.enabled = x;
                s.width.enabled = x;
                s.height.enabled = x;
                s.pxsize.enabled = x;
            }
        };
        gamemode.setSize(96,32);
        addObject(gamemode, 7,3);

        gml = new Label("Gamemode");
        gml.setSize(96,32);
        gml.setTextColor(Color.WHITE);
        gml.setBackgroundColor(Color.BLACK);
        addObject(gml, 3,3);

        Snakeworld.Gamemode selected = Snakeworld.Gamemode.from((String) gamemode.getSelected());
        System.out.println(selected);

        startsize = new Textfield();
        startsize.setText(selected.start_size+"");
        addObject(startsize, 7,4);

        szl = new Label("Startlength");
        szl.setSize(96,32);
        szl.setTextColor(Color.WHITE);
        szl.setBackgroundColor(Color.BLACK);
        addObject(szl, 3,4);

        width = new Textfield();
        width.setSize(96,32);
        width.setText(selected.width+"");
        addObject(width, 7, 5);

        wl = new Label("width");
        wl.setSize(96,32);
        wl.setTextColor(Color.WHITE);
        wl.setBackgroundColor(Color.BLACK);
        addObject(wl, 3,5);

        height = new Textfield();
        height.setText(""+selected.height);
        addObject(height, 7, 6);

        hl = new Label("height");
        hl.setSize(96,32);
        hl.setTextColor(Color.WHITE);
        hl.setBackgroundColor(Color.BLACK);
        addObject(hl, 3,6);

        pxsize = new Textfield();
        pxsize.setText(""+selected.pxsize);
        addObject(pxsize, 7,7);

        pxl = new Label("pixel size");
        pxl.setSize(96,32);
        pxl.setTextColor(Color.WHITE);
        pxl.setBackgroundColor(Color.BLACK);
        addObject(pxl, 3,7);

        start = new Button() {
            @Override
            public void clickEvent(MouseEventInfo ee) {
                Snakeworld.Gamemode g = Snakeworld.Gamemode.from((String) gamemode.getSelected());
                Settingsscreen s = (Settingsscreen) world;
                if (g == Snakeworld.Gamemode.CUSTOM) {
                        g = new Snakeworld.Gamemode("custom", Integer.parseInt(s.width.text), Integer.parseInt(s.height.text), Integer.parseInt(s.pxsize.text), Integer.parseInt(s.startsize.text), 0, 0);
                }
                try {
                    World w = new Snakeworld(g, Deathscreen.stats, Utils.loadImageFromAssets("background_tile"), 1);
                    setTps(Integer.parseInt(tps.text));
                    System.out.println(e.tps);
                    World.switchWorld(w);
                }
                    catch(IllegalArgumentException ex) {
                    Label inv = new Label("Invalid arguments. Parameters must not be 0");
                    inv.setSize(400,32);
                    inv.setTextColor(Color.RED);
                    inv.setBackgroundColor(Color.BLACK);
                    addObject(inv, 5,9);
                }

            }

            @Override
            public void tick() {

            }
        };
        start.setText("Start");
        start.setTextColor(Color.WHITE);
        start.setSize(96,32);
        start.setBackgroundColor(Color.BLUE);
        addObject(start, 5,10);

        tps = new Textfield();
        tps.setText((int)e.tps+"");
        addObject(tps, 7,8);

        tpsl = new Label("Ticks per second");
        tpsl.setBackgroundColor(Color.BLACK);
        tpsl.setSize(150,32);
        tpsl.setTextColor(Color.WHITE);
        addObject(tpsl, 3,8);

        exit = new Button() {
            @Override
            public void clickEvent(MouseEventInfo e) {
                World.mainframe.dispose();
            }

            @Override
            public void tick() {

            }
        };
        exit.setSize(96,32);
        exit.setText("Exit");
        exit.setBackgroundColor(Color.RED);
        addObject(exit, 5,12);


        if ("custom".equals(gamemode.selected)) {
            width.setText(Snakeworld.g.width+"");
            height.setText(Snakeworld.g.height+"");
            pxsize.setText(Snakeworld.g.pxsize+"");
            startsize.setText(Snakeworld.g.start_size+"");
        }
        objectsOf(Textfield.class).forEach(tf -> {
            tf.setSize(96,32);
            tf.setBorderColor(Color.WHITE);
            tf.setSelectedBorderColor(Color.GREEN);
            tf.enabled = false;
            tf.isNumeric = true;
            tf.maxsize=2;
        });

        startsize.maxsize = -1;
        tps.enabled = true;
    }

    @Override
    public void tick() {

    }

    public List<AdvancedImage> colorOptions() {
        List<AdvancedImage> imgs =  new ArrayList<>();
        List<Color> c = new ArrayList<>();
//        Arrays.stream(Color.class.getFields()).filter(f -> Modifier.isStatic(f.getModifiers())).forEach(clr -> {
//            AdvancedImage img = new AdvancedImage(64,64);
//            try {
//                img.fill((Color) clr.get(Color.BLACK));
//                imgs.add(img);
//            } catch (IllegalAccessException illegalAccessException) {
//                illegalAccessException.printStackTrace();
//            }
//        });
        c.add(Color.BLUE);
        c.add(Color.GRAY);
        c.add(Color.GREEN);
        c.add(Color.ORANGE);
        c.add(Color.WHITE);
        c.add(Color.CYAN);
        c.add(Color.YELLOW);
        c.add(Color.RED);
        c.add(Color.PINK);
        c.add(new Color(0,150,0));
        c.add(new Color(0,100,0));
        c.add(new Color(92,206,250));
        c.add(new Color(0x124816));
        c.add(new Color(0x112358));
        c.add(new Color(0xBADA55));
        c.add(new Color(0xC0FFEE));
        c.add(new Color(0xBADC0DE));
        c.add(new Color(0xBADC0DED));
        c.add(new Color(0xC0DE));
        c.add(new Color(0xC0DEBA5E));
        c.add(new Color(0xDA7A));
        c.add(new Color(0xB17));
        c.add(new Color(0xDEC0DE));
        c.add(new Color(0xDEFACE));
        c.add(new Color(0xACCE55));
        c.add(new Color(0xC00C1E));
        c.add(new Color(0xE66));
        c.add(new Color(0xACE));
        c.add(new Color(0xB1));
        c.add(new Color(0xC15));
        c.add(new Color(0xB16B00B5));
        c.add(new Color(0x71DD1E5));

        for (Color clr : c) {
            AdvancedImage img = new AdvancedImage(64,64);
            img.fill(clr);
            imgs.add(img);
        }

        return imgs;
    }

    public void windowClosed(WindowEvent e) {
        Filework.writeScores(Deathscreen.stats);
    }
}
