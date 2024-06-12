package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static View.Pictures.*;

/**
 * Megjeleníti a menüt, leszármazik a Window osztályból
 */
public class MenuView extends Window {

    Button newGame, exit;

    private BufferedImage background =  ImageUtility.backgroundScale(Pictures.background);
    private BufferedImage bgText = ImageUtility.scaleImage(menuTitle, 250);

    /**
     * Konstruktor
     * Beállítja a gombokat, és hozzáadja őket a drawables és a components listához
     */
    public MenuView() {
        newGame = new Button(760, 250, 250 + 250, 50, this);

        exit = new Button(760, 370, 250 + 250, 50, this);

        newGame.text = "Új játék";
        newGame.img = ImageUtility.scaleImage(playButton, 50);

        newGame.option = WindowOptions.newgame;

        exit.text = "Kilépés";
        exit.img = ImageUtility.scaleImage(exitButton, 50);
        exit.option = WindowOptions.exit;
        addDrawable(newGame, true);
        addClickable(newGame, true);
        addDrawable(exit, true);
        addClickable(exit, true);



    }

    /**
     * Kirajzolja a menüt
     * @param g - a kirjzolásért felelős Graphics objektum
     */
    @Override
    public void paint(Graphics g) {
        AppFrame.setGraphicQuality(g);
        g.drawImage(background, 0, 0, null);
        g.drawImage(bgText, 50, 50, null);


        g.setFont(new Font("Inter", Font.BOLD, 30));
        g.drawString(newGame.text, 815 + 25, 250 + 35);
        g.drawString(exit.text, 815 + 25, 370 + 35);

        for (Drawable d : drawables) {
            d.paint(g);
        }
        for (JComponent c : components) {
            c.paint(g);
        }
    }

    /**
     * Frissíti a menüt
     */
    @Override
    public void update() {
        for (Drawable d : drawables) {
            d.update();
        }
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
     * A menüben a egérrel lehet navigálni
     * @param e - kattintás
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // megnézi, hogy melyik gombra kattintottunk
        //az benne van-e a hitboxban
        // ha igen, akkor meghívja a gombhoz tartozó akciót
        for (Clickable c : clickables) {
            if (c.isIn(e)) {
                c.clickAction(e);
            }
        }
    }
}
