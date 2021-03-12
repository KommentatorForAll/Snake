package Enginegames.Snake;

import Enginegames.*;
import Enginegames.Button;
import Enginegames.Label;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Settingsscreen extends World {

    public Textfield startsize, width, height, pxsize, tps;
    public ScrollButton primaryColor, secondaryColor, tertiaryColor, gamemode;
    public Label pcl, gml, szl, wl, hl, pxl, tpsl, title, ssal, sssl;
    public Button start, exit, swp, nRndColors, skinSetApple, skinSetSnake;
    public static boolean skin, usrInputtedColor;
    public List<AdvancedImage> clrs = colorOptions();
    public static Color primary, secondary, tertiary;
    public static int ssai = 2, sssi = 3, tick;

    public static Map<String, Map<String, AdvancedImage>> sprites = Filework.loadAllSprites();

    public Settingsscreen() {
        super(10,12,64);
        Button.defaultFont = Snakeworld.fonts.get("Welbut").deriveFont(15F);
        AdvancedImage img = new AdvancedImage(64,64);
        img.fill(Color.BLACK);
        setBackground(img);
        img.imgs = AdvancedImage.ImageSizing.STRETCH;
        gameSkins();
        gameSettings();
        setTps(20);
    }

    @Override
    public void tick() {
        tick = (tick+1)%360+1;
    }

    public void gameSettings() {
        removeAll();
        addCommon();
        if (gamemode == null) {
            List<String> gmimgs = Arrays.stream(Snakeworld.Gamemode.values).map(g -> g.name).map(String::toLowerCase).collect(Collectors.toList());
            gamemode = new ScrollButton(gmimgs, gmimgs.stream().filter(g -> Snakeworld.g.name.equals(g)).findFirst().orElse("default")) {
                public void clickEvent(MouseEventInfo e) {
                    super.clickEvent(e);
                    Settingsscreen s = (Settingsscreen) world;
                    Snakeworld.Gamemode g = Snakeworld.Gamemode.from((String) selected);
                    s.startsize.setText(g.start_size + "");
                    s.width.setText(g.width + "");
                    s.height.setText(g.height + "");
                    s.pxsize.setText(g.pxsize + "");

                    boolean x = g.name.equals("custom");
                    s.startsize.enabled = x;
                    s.width.enabled = x;
                    s.height.enabled = x;
                    s.pxsize.enabled = x;
                }
            };
            gamemode.setSize(96, 32);

            gml = new Label("Gamemode");
            gml.setSize(96, 32);
            gml.setTextColor(Color.WHITE);
            gml.setBackgroundColor(Color.BLACK);

            Snakeworld.Gamemode selected = Snakeworld.Gamemode.from((String) gamemode.getSelected());
            System.out.println(selected);

            startsize = new Textfield();
            startsize.setText(selected.start_size + "");

            szl = new Label("Startlength");
            szl.setSize(96, 32);
            szl.setTextColor(Color.WHITE);
            szl.setBackgroundColor(Color.BLACK);

            width = new Textfield();
            width.setSize(96, 32);
            width.setText(selected.width + "");

            wl = new Label("width");
            wl.setSize(96, 32);
            wl.setTextColor(Color.WHITE);
            wl.setBackgroundColor(Color.BLACK);

            height = new Textfield();
            height.setText("" + selected.height);

            hl = new Label("height");
            hl.setSize(96, 32);
            hl.setTextColor(Color.WHITE);
            hl.setBackgroundColor(Color.BLACK);

            pxsize = new Textfield();
            pxsize.setText("" + selected.pxsize);

            pxl = new Label("pixel size");
            pxl.setSize(96, 32);
            pxl.setTextColor(Color.WHITE);
            pxl.setBackgroundColor(Color.BLACK);

            tps = new Textfield();
            tps.setText((int) e.tps + "");

            tpsl = new Label("Ticks per second");
            tpsl.setBackgroundColor(Color.BLACK);
            tpsl.setSize(150, 32);
            tpsl.setTextColor(Color.WHITE);

            if ("custom".equals(gamemode.selected)) {
                width.setText(Snakeworld.g.width + "");
                height.setText(Snakeworld.g.height + "");
                pxsize.setText(Snakeworld.g.pxsize + "");
                startsize.setText(Snakeworld.g.start_size + "");
            }

            startsize.maxsize = -1;
            tps.enabled = true;
        }
        addObject(gamemode, 7, 3);
        addObject(gml, 3, 3);
        addObject(tpsl, 3, 8);
        addObject(tps, 7, 8);
        addObject(pxl, 3, 7);
        addObject(pxsize, 7, 7);
        addObject(hl, 3, 6);
        addObject(height, 7, 6);
        addObject(wl, 3, 5);
        addObject(width, 7, 5);
        addObject(szl, 3, 4);
        addObject(startsize, 7, 4);
        objectsOf(Textfield.class).forEach(tf -> {
            tf.setSize(96, 32);
            tf.setBorderColor(Color.WHITE);
            tf.setSelectedBorderColor(Color.GREEN);
            tf.enabled = false;
            tf.isNumeric = true;
            tf.maxsize = 2;
        });
        tps.enabled = true;
    }

    public void addCommon() {
        title = new Label("settings") {
            public void tick() {
                setTextColor(Color.getHSBColor((float)(tick/360.0),1,1));
            }
        };
        title.setFont(Snakeworld.fonts.get("pixel-bubble").deriveFont(40F));
        title.setBackgroundColor(Color.BLACK);
        title.setBackgroundHoverColor(Color.BLACK);
        addObject(title, 5,1);



        start = new Button() {
            @Override
            public void clickEvent(MouseEventInfo ee) {
                Snakeworld.Gamemode g = Snakeworld.Gamemode.from((String) gamemode.getSelected());
                Settingsscreen s = (Settingsscreen) world;
                if (g == Snakeworld.Gamemode.CUSTOM) {
                    g = new Snakeworld.Gamemode("custom", Integer.parseInt(s.width.text), Integer.parseInt(s.height.text), Integer.parseInt(s.pxsize.text), Integer.parseInt(s.startsize.text), 0, 0);
                }
                try {
                    setTps(Integer.parseInt(tps.text));
                    primary = new Color(((AdvancedImage)primaryColor.selected).getRGB(10,10));
                    secondary = new Color(((AdvancedImage)secondaryColor.selected).getRGB(10,10));
                    tertiary = new Color(((AdvancedImage)tertiaryColor.selected).getRGB(10,10));
                    String name = (String) sprites.get("head").keySet().toArray()[sssi];
                    Tile.corner = sprites.get("corner").get(name).replaceColor(new Color(0x00ff0000, true), primary).replaceColor(new Color(0x0000ff00,true), secondary).replaceColor(new Color(0x000000ff, true), tertiary);
                    Tile.line = sprites.get("mid").get(name).replaceColor(new Color(0x00ff0000, true), primary).replaceColor(new Color(0x0000ff00,true), secondary).replaceColor(new Color(0x000000ff, true), tertiary);
                    Tile.tail = sprites.get("tail").get(name).replaceColor(new Color(0x00ff0000, true), primary).replaceColor(new Color(0x0000ff00,true), secondary).replaceColor(new Color(0x000000ff, true), tertiary);
                    Head.commonImage = sprites.get("head").get(name).replaceColor(new Color(0x00ff0000, true), primary).replaceColor(new Color(0x0000ff00,true), secondary).replaceColor(new Color(0x000000ff, true), tertiary);
                    Tile.line = Tile.line.rotate(90);
                    Tile.tail = Tile.tail.rotate(-90);
                    Apple.skin = (AdvancedImage) sprites.get("apple").values().toArray()[ssai];
                    Head.commonImage = Head.commonImage.rotate(90);
                    World w = new Snakeworld(g, Deathscreen.stats, Utils.loadImageFromAssets("background_tile"), 1);
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
        addObject(start, 3,10);

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
        addObject(exit, 7,10);

        swp = new Button() {
            @Override
            public void clickEvent(MouseEventInfo e) {
                if (skin) {
                    gameSettings();
                }
                else {
                    gameSkins();
                }
                skin = !skin;
            }

            @Override
            public void tick() {

            }
        };
        swp.setText(!skin?"game settings":"skin settings");
        swp.setSize(128,32);
        swp.setBackgroundColor(Color.ORANGE);
        swp.setTextColor(Color.BLACK);
        addObject(swp, 8,0);
    }

    public void gameSkins() {
        removeAll();
        addCommon();

        if (primaryColor == null) {

            primaryColor = new ScrollButton(clrs, clrs.get(0)){
                public void clickEvent(MouseEventInfo e) {
                    super.clickEvent(e);
                    usrInputtedColor = true;
                    skinSetSnake._update();
                }
            };

            pcl = new Label("Colors");
            pcl.setTextColor(Color.WHITE);
            pcl.setBackgroundColor(Color.BLACK);
            pcl.setSize(96, 32);

            secondaryColor = new ScrollButton(clrs, clrs.get(0)){
                public void clickEvent(MouseEventInfo e) {
                    super.clickEvent(e);
                    usrInputtedColor = true;
                    skinSetSnake._update();
                }
            };

            tertiaryColor = new ScrollButton(clrs, clrs.get(0)){
                public void clickEvent(MouseEventInfo e) {
                    super.clickEvent(e);
                    usrInputtedColor = true;
                    skinSetSnake._update();
                }
            };

            nRndColors = new Button() {
                @Override
                public void clickEvent(MouseEventInfo e) {
                    primaryColor.selectRandom();
                    secondaryColor.selectRandom();
                    tertiaryColor.selectRandom();
                    usrInputtedColor = false;
                    skinSetSnake._update();
                }

                @Override
                public void tick() {

                }
            };
            nRndColors.setSize(128,32);
            nRndColors.setTextColor(Color.WHITE);
            nRndColors.setBackgroundColor(Color.BLUE);
            nRndColors.setText("random Colors");

            System.out.println(sprites);
            skinSetSnake = new Button() {
                @Override
                public void clickEvent(MouseEventInfo e) {
                    sssi = (sssi+1)%sprites.get("head").values().size();
                    _update();
                }

                public void _update() {
                    primary = new Color(((AdvancedImage)primaryColor.selected).getRGB(10,10));
                    secondary = new Color(((AdvancedImage)secondaryColor.selected).getRGB(10,10));
                    tertiary = new Color(((AdvancedImage)tertiaryColor.selected).getRGB(10,10));
                    Color p = Settingsscreen.primary, s = Settingsscreen.secondary, t = Settingsscreen.tertiary;
                    AdvancedImage tmp =(AdvancedImage) sprites.get("head").values().toArray()[sssi];
                    tmp.imgs = AdvancedImage.ImageSizing.STRETCH;
                    tmp = tmp.replaceColor(new Color(0x00ff0000, true), p).replaceColor(new Color(0x0000ff00,true), s).replaceColor(new Color(0x000000ff, true), t);
                    setBackgroundImage(tmp);
                }

                @Override
                public void tick() {

                }
            };
            skinSetSnake.setSize(64,64);

            sssl = new Label("Snake skin:");
            sssl.setSize(96,64);
            sssl.setTextColor(Color.WHITE);
            sssl.setBackgroundColor(Color.BLACK);

            skinSetApple = new Button() {
                @Override
                public void clickEvent(MouseEventInfo e) {
                    ssai = (ssai+1)%sprites.get("apple").values().size();
                    _update();
                }

                public void _update() {
                    AdvancedImage tmp = (AdvancedImage) sprites.get("apple").values().toArray()[ssai];
                    tmp = tmp.scale(64,64);
                    setBackgroundImage(tmp);
                }
                @Override
                public void tick() {

                }
            };
            skinSetApple.setSize(64,64);


            ssal = new Label("Apple skin:");
            ssal.setSize(96,64);
            ssal.setTextColor(Color.WHITE);
            ssal.setBackgroundColor(Color.BLACK);

            if (primary != null) {
                primaryColor.select(primaryColor.options.stream().filter(i -> ((AdvancedImage) i).getRGB(10,10) == primary.getRGB()).findFirst().orElse(primaryColor.options.get(0)));
                secondaryColor.select(secondaryColor.options.stream().filter(i -> ((AdvancedImage) i).getRGB(10,10) == secondary.getRGB()).findFirst().orElse(secondaryColor.options.get(0)));
                tertiaryColor.select(tertiaryColor.options.stream().filter(i -> ((AdvancedImage) i).getRGB(10,10) == tertiary.getRGB()).findFirst().orElse(tertiaryColor.options.get(0)));
            }
            else {
                primaryColor.selectRandom();
                secondaryColor.selectRandom();
                tertiaryColor.selectRandom();
            }
        }
        if (!usrInputtedColor) {
            primaryColor.selectRandom();
            secondaryColor.selectRandom();
            tertiaryColor.selectRandom();
        }
        skinSetApple._update();
        skinSetSnake._update();

        addObject(primaryColor, 3, 5);
        addObject(secondaryColor, 5, 5);
        addObject(tertiaryColor, 7, 5);
        addObject(pcl, 1, 5);
        addObject(nRndColors, 5,6);
        addObject(skinSetSnake, 7,7);
        addObject(sssl, 3,7);
        addObject(skinSetApple,7,8);
        addObject(ssal,3,8);
        objectsOf(ScrollButton.class).forEach(sb -> {
            sb.setSize(64, 64);
        });
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
        c.add(new Color(0xE));

        for (Color clr : c) {
            AdvancedImage img = new AdvancedImage(64,64);
            img.fill(clr);
            imgs.add(img);
        }

        return imgs;
    }

    public void windowClosed(WindowEvent e) {
        System.out.println("saving stats");
        Filework.writeScores(Deathscreen.stats);
    }
}
