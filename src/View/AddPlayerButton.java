package View;

import Controller.Controller;
import Model.PointCounter;

import java.awt.event.MouseEvent;

/**
 * Játékos hozzáadásáért felelős gomb
 */
public class AddPlayerButton extends Button {
    int type;
    String name;
    static int counter = 0;

    CharacterCreation cc = null;

    // type = 1 -> add new player
    // type = -1 -> remove player
    public AddPlayerButton(int x, int y, int width, int height, Window v, int type, String name) {
        super(x, y, width, height, v);
        this.type = type;
        this.name = name;
    }

    /**
     * Játékos hozzáadása kattintásra
     *
     * @param e kattintás event
     */
    public void clickAction(MouseEvent e) {
        String temp = name + counter;
        counter++;
        // játékos hozzáadása
        if (type == 1) {
            if (name.equals("mechanic")) { // ha a játékos mechanic
                // hozzáadás a controllerhez
                Controller.getInstance().create(temp, name, 0, 0);
                // karakter nevének és színének megadása
                cc = new CharacterCreation(temp, name);
            } else if (name.equals("saboteur")) { // ha a játékos saboteur
                // hozzáadás a controllerhez
                Controller.getInstance().create(temp, name, 0, 0);
                // karakter nevének és színének megadása
                cc = new CharacterCreation(temp, name);
            }

        } else if (type == -1) {
            if (name.equals("mechanic")) {

            } else if (name.equals("saboteur")) {

            }
        }


    }
}
