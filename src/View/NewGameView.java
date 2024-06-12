package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import Model.PointCounter;

import static View.Pictures.*;

/**
 * Megjeleníti az új játkot, leszármazik a Window osztályból
 */
public class NewGameView extends Window {
    Button start, exit, map;
    ChangeMaxPointButton increase, decrease;
    AddPlayerButton addSaboteur, addMechanic;
    int screenWidth;
    int screenHeight;

    /**
     * map lekérdezése
     * @return map
     */
    public int getMap() {
        return mapper;
    }
    /**
     * map beállítása
     * @param map beállítandó map
     */
    public void setMap(int map) {
        this.mapper = map;
    }

    int mapper = 1;

    /**
     * Konstruktor
     * Beállítja a gombokat, és hozzáadja őket a drawables és a components listához
     */
    public NewGameView() {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = (int) screenSize.getWidth() * 3 / 4;
        screenHeight = (int) screenSize.getHeight() * 3 / 4;
        start = new Button(screenWidth - 200, 10, 50, 50, this);
        exit = new Button(screenWidth - 100, 10, 150, 150, this);
        map = new Button(50, screenHeight / 2, 500, 300, this);
        map.option = WindowOptions.map1;
        map.img = ImageUtility.scaleImage(mapPrevButton, 200);

        int offset = 25;
        addSaboteur = new AddPlayerButton(map.x+map.img.getWidth()+140+220, screenHeight / 2 - 35 + 190 + offset, 35, 35, this, 1, "saboteur");
        addMechanic = new AddPlayerButton(map.x+map.img.getWidth()+140, screenHeight / 2 - 35 + 190 + offset, 35, 35, this, 1, "mechanic");



        increase = new ChangeMaxPointButton(screenWidth / 2 + 16 * 13, screenHeight / 6 - 8, 35, 35, this, 1);
        decrease = new ChangeMaxPointButton(screenWidth / 2 + 20 * 13, screenHeight / 6 - 8, 35, 35, this, -1);

        start.img = ImageUtility.scaleImage(playButton, 50);
        start.option = WindowOptions.game;
        exit.img = ImageUtility.scaleImage(exitButton, 50);
        exit.option = WindowOptions.exit;
        addMechanic.img = ImageUtility.scaleImage(plusButton, 35);
        addSaboteur.img = ImageUtility.scaleImage(plusButton, 35);
        increase.img = ImageUtility.scaleImage(plusButton, 35);
        decrease.img = ImageUtility.scaleImage(minusButton, 35);

        addDrawable(start, true);
        addDrawable(exit, true);
        addDrawable(map, true);
        addDrawable(addMechanic, true);
        addDrawable(addSaboteur, true);
        addDrawable(increase, true);
        addDrawable(decrease, true);

        addClickable(start, true);
        addClickable(exit, true);
        addClickable(map, true);
        addClickable(addMechanic, true);
        addClickable(addSaboteur, true);
        addClickable(increase, true);
        addClickable(decrease, true);


    }

    /**
     * Kirajzolja a NewGameView-t
     * @param g kirajzolásért felelős Graphics objektum
     */
    @Override
    public void paint(Graphics g) {
        AppFrame.setGraphicQuality(g);
        Graphics2D graphics2D = (Graphics2D) g;
        // betűméret és típus beállítása
        int fontSize = 150;
        graphics2D.setFont(new Font("Inter", Font.BOLD, fontSize));
        // Újjáték felirat kirajzolása
        String newGame = "Új játék";
        graphics2D.drawString(newGame, 15, fontSize + 30);
        // betűméret és típus beállítása
        fontSize = 30;
        graphics2D.setFont(new Font("Inter", Font.BOLD, fontSize));
        // Másik térkép kirajzolása
        String othermap = "Másik térkép választása...";
        graphics2D.drawString(othermap, screenWidth / 5-80, screenHeight / 2 - 55);
        graphics2D.setStroke(new BasicStroke(4));
        graphics2D.drawRect(map.x-30, map.y-50, map.img.getWidth()+60, map.img.getHeight()+100);
        //Célpontszám kiralzolása
        String pointsToWin = "Célpontszám: ";
        String points = "" + PointCounter.getInstance().GetPointsToWin();
        g.drawString(points, screenWidth / 2 + 75, screenHeight / 6 + 25);
        g.drawString(pointsToWin, screenWidth / 2, screenHeight / 6 - 35);
        // betűméret és típus beállítása
        fontSize = 30;
        g.setFont(new Font("Inter", Font.BOLD, fontSize));
        // Játékosok hozzáadása felirat kirajzolása
        // Felvett játékosok kirajzolása
        String mechanics = "A Szerelők";
        g.drawString(mechanics, map.x+map.img.getWidth()+110, screenHeight / 2 - 60);
        String saboteurs = "A Szabotőrök";
        g.drawString(saboteurs, map.x+map.img.getWidth()+110+200, screenHeight / 2 - 60);

        // képek kirajzoásához tartozó téglalapok kirajzolása
        g.drawRect(map.x+map.img.getWidth()+80, screenHeight / 2 - 90, 220, 40);
        g.drawRect(map.x+map.img.getWidth()+80+220, screenHeight / 2 - 90, 220, 40);
        g.drawRect(map.x+map.img.getWidth()+80, screenHeight / 2 -50, 220, 300);
        g.drawRect(map.x+map.img.getWidth()+80+220, screenHeight / 2 -50, 220, 300);

        // Kirajzolás
        for (Drawable d : drawables) {
            d.paint(g);
        }

        for (JComponent c : components) {
            c.paint(g);
        }

    }

    /**
     * Frissítés
     */
    @Override
    public void update() {

    }

    /**
     * A billentyűlenyomásokat kezeli
     * @param e leütött billentyű
     */
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    /**
     * Kattintás kezelése
     * @param e egérkattintás
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        for (Clickable c : clickables) {
            if (c.isIn(e)) {
                c.clickAction(e);
            }
        }
    }


}
