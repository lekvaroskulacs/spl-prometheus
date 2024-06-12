package View;

import Controller.Controller;
import Model.PointCounter;

import java.awt.event.MouseEvent;

/**
 * Pont beállításért felelős gomb, leszármazottja a Button osztálynak
 */
public class ChangeMaxPointButton extends Button {
    int type;

    /**
     * Konstruktor, beállítja a paramétereket
     * @param x - x koordináta
     * @param y - y koordináta
     * @param width - szélesség
     * @param height - magasság
     * @param v - ablak
     * @param type - típus
     */
    public ChangeMaxPointButton(int x, int y, int width, int height, Window v, int type) {
        super(x, y, width, height, v);
        this.type = type;
    }

    /**
     * Pont hozzáadása vagy levonása kattintásra
     * @param e - kattintás event
     */
    public void clickAction(MouseEvent e) {
        // növelés vagy csökkentés mértéke
        int increment = 10;

        if (type == 1) { // ha növelni kell
            int temp = PointCounter.getInstance().GetPointsToWin() + type * increment;
            PointCounter.getInstance().setPointsToWin(temp);
        } else if (type == -1) { // ha csökkenteni kell
            int temp = PointCounter.getInstance().GetPointsToWin() + type * increment;
            if (temp > 0) { // ha a pontszám nem negatív
                PointCounter.getInstance().setPointsToWin(temp);
            }
        }

        // képernyő újrarajzolása
        Controller.getInstance().getFrame().repaint();

    }
}
