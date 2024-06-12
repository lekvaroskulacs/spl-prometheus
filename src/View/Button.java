package View;

import Controller.Controller;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import static View.Pictures.*;

/**
 * Button - gomb osztály
 */
public class Button extends Drawable implements Clickable {

    /**
     * Button konstruktor
     * @param x - gomb x koordinátája
     * @param y - gomb y koordinátája
     * @param width - gomb szélessége
     * @param height - gomb magassága
     * @param v - ablak, amelyen a gomb megjelenik
     */
    public Button(int x, int y, int width, int height, Window v) {
        super(x, y, v);
        buttonBox = new Rectangle(x, y, width, height);
    }
    protected WindowOptions option;
    protected Rectangle buttonBox;
    protected BufferedImage img;
    protected String text;

    /**
     * Button kirajzolása
     * @param g - kirajzoló grafikus objektum
     */
    @Override
    public void paint(Graphics g) {
        AppFrame.setGraphicQuality(g);
        g.drawImage(img,x,y,null);
    }

    /**
     * Button frissítése
     */
    @Override
    public void update() {

    }

    /**
     * A gombhoz tartozó hitboxon bellül kapott kattintás kezelése
     * @return - hitboxon belüli kattintás esetén true, egyébként false
     */
    @Override
    public boolean isIn(MouseEvent e) {

        if(buttonBox.contains(e.getPoint())){
            clickAction(e);
        }

        return false;
    }

    /**
     * A gombhoz tartozó kattintás kezelése
     * @param e - egérkattintás
     */
    @Override
    public void clickAction(MouseEvent e) {
        // kilépés megvalósítása
        if(option == WindowOptions.exit)
            System.exit(0);
        if (option == null)
            return;

        // map változtatása
        if(this.option == WindowOptions.map1){
            this.option = WindowOptions.map2;
            Controller.getInstance().getNewGameView().setMap(2);
            img =  ImageUtility.scaleImage(mappreview2, 200);
            return;
        }
        if(this.option == WindowOptions.map2){
            this.option = WindowOptions.map1;
            Controller.getInstance().getNewGameView().setMap(1);
            img = ImageUtility.scaleImage(mapPrevButton, 200);
            return;
        }
        // játék indítása
        WindowOptions.windowOption = option;
        if(this.option == WindowOptions.game){

            if (Controller.getInstance().getMechanics().size() >= 2
            && Controller.getInstance().getSaboteurs().size() >= 2) {

                if (Controller.getInstance().getNewGameView().getMap() == 1) {
                    Controller.getInstance().getGameView().mapSetup(System.getProperty("user.dir") + File.separator + "src" + File.separator + "Assets" + File.separator + "map1.txt");
                } else if (Controller.getInstance().getNewGameView().getMap() == 2) {
                    Controller.getInstance().getGameView().mapSetup(System.getProperty("user.dir") + File.separator + "src" + File.separator + "Assets" + File.separator + "map2.txt");
                }
            } else {
                WindowOptions.windowOption = WindowOptions.newgame;
            }
        }



        // Kirajzolás
        Controller.getInstance().getFrame().repaint();

    }

    /**
     * A gomb mozgathatóságának lekérdezése
     * @return - mozgatható-e a gomb
     */
    @Override
    public boolean isDraggable() {
        return false;
    }

    @Override
    public Object getCorrespondingModelElement() {
        return null;
    }

}
