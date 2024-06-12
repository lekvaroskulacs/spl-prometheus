package View;

import Model.ActionType;
import Model.Player;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.LinkedList;
/**
 * A játékban megjelenő popup menü osztálya
 */
public class PopUpBar extends Drawable {
    /**
     * Konstruktor
     * @param x x koordináta
     * @param y y koordináta
     * @param r sugár
     * @param v Window
     * @param creator a létrehozó SteppableView
     * @param at a létrehozó SteppableView által elérhető Action-ok
     */
    public PopUpBar(int x, int y, int r, Window v, SteppableView creator, ActionType[] at) {
        super(x, y, v);
        this.r = r;

        buttonLocations = new Point[6];
        for (int i = 0; i < buttonLocations.length; ++i) {
            buttonLocations[i] = new Point();
            buttonLocations[i].x = (int) (Math.cos(Math.toRadians(i * -60 - 90)) * r + x);
            buttonLocations[i].y = (int) (Math.sin(Math.toRadians(i * -60 - 90)) * r + y);

        }

        Player p = Controller.Controller.getInstance().getActivePlayer();
        if (at == null)
            at = p.availableActions(creator.getCorrespondingModelSteppable());

        for (int i = 0; i < buttonLocations.length; ++i) {
            if (at[i] != null) {
                buttons.add(new PopUpButton(buttonLocations[i].x, buttonLocations[i].y, 30, at[i], view, creator));
            }
        }
        for (PopUpButton b : buttons) {
            view.addClickable(b, true);
            view.addDrawable(b, true);
        }
        this.creator = creator;
    }

    private SteppableView creator;

    public SteppableView getCreator() {
        return creator;
    }

    private Point[] buttonLocations;
    private int r;

    private List<PopUpButton> buttons = new LinkedList<PopUpButton>();
    /**
     * A popup menü kirajzolása
     * @param g Graphics
     */
    public void paint(Graphics g) {
        g.setColor(Color.RED);
        for (PopUpButton button : buttons) {
            button.paint(g);
        }

    }
    /**
     * A popup menü frissítése
     */
    @Override
    public void update() {

    }

    @Override
    public Object getCorrespondingModelElement() {
        return null;
    }
    /**
     * Elem törlése
     */
    public void delete() {
        for (PopUpButton b : buttons)
            view.remove(b);
    }

    /**
     * A popup menüben lévő gombok hitboxának lekérdezése
     * @param e MouseEvent
     * @return benne van-e a gombok hitboxában
     */
    public boolean inButtons(MouseEvent e) {
        for (PopUpButton b : buttons)
            if (b.isIn(e))
                return true;
        return false;
    }

    /**
     * A popup menüben lévő gombok lekérdezése
     * @return gombok
     */
    public List<PopUpButton> getButtons() {
        return buttons;
    }
}
