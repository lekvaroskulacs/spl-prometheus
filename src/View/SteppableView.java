package View;

import Model.Player;
import Model.Steppable;

import java.awt.*;

/**
 * A játékban kirajzolható objektumok interfésze
 */
public interface SteppableView {

    /**
     * Visszaadja, hogy egy új játékost melyik pontban lehet kirajzolni
     */
    public Point getDefaultPlayerPosition();

    /**
     * Játékos szögének meghatározása
     * @param p - játékos
     * @return - szög
     */
    public double getPlayerAngle(Player p);
    /**
     * Forgatás középpontjának lekérdezése
     * @return - forgatás középpontja
     */
    public Point getRotationCenter();

    public Steppable getCorrespondingModelSteppable();
}
