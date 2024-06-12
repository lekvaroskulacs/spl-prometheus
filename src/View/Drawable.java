package View;

import javax.swing.*;
import java.awt.*;

/**
 * Drawable - kirajzolható objektumok absztrakt ősosztálya, leszármazottja a JComponent osztály
 */
public abstract class Drawable extends JComponent {
    protected Window view;
    protected int x;
    protected int y;

    /**
     * A kirajzolásért felelős függvény
     * @param g - a kirajzolásért felelős Graphics objektum
     */
    @Override
    public abstract void paint(Graphics g);

    /**
     * Rajzolható objektumok konstruktora
     * @param x - x koordináta
     * @param y - y koordináta
     * @param v - a hozzá tartozó ablak
     */
    public Drawable(int x, int y, Window v) {
        this.x = x;
        this.y = y;
        this.view=v;
    }

    /**
     * Frissíti a kirajzolható objektumot
     */
    public abstract void update();

    public abstract Object getCorrespondingModelElement();

    /**
     *  Megjelenítés
     * @param x - x koordináta
     * @param y - y koordináta
     * @param graphics2D - a kirajzolásért felelős Graphics2D objektum
     */
    public void paintOnPlayer(int x, int y, Graphics2D graphics2D){

    }

}
