package View;

import Model.Player;
import Model.Spring;
import Model.Steppable;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

import static View.Pictures.*;

/**
 * Forrás kirajzolásáért felelős osztály, leszármazottja a Drawable osztálynak, megvalósítja a Clickable, CreatePopUpBar és SteppableView interfészeket.
 */
public class SpringView extends Drawable implements Clickable, CreatePopUpBar, SteppableView {
    /**
     * Konstruktor
     * @param x - x koordináta
     * @param y - y koordináta
     * @param r - sugár
     * @param s - forrás
     * @param v - ablak
     */
    public SpringView(int x, int y, int r, Spring s, Window v) {
        super(x, y, v);
        this.r = r;
        spring = s;
        sprite = springFilledImg;
        sprite = ImageUtility.scaleImage(sprite, 2*r);
        arrowSprite = pumpIndicatorImg;
        arrowSprite = ImageUtility.scaleImage(arrowSprite, 20);
        if (WindowOptions.windowOption == WindowOptions.game)
            gameView = (GameView) v;
    }
    private GameView gameView;
    private BufferedImage sprite = null;
    private Spring spring;
    private int r;
    private int arrowLocX;
    private int arrowLocY;
    private double arrowAngle;
    private PipeView outPipe = null;
    private BufferedImage arrowSprite = null;

    /**
     * A forrás kirajzolásáért felelős függvény
     * @param g - grafikus objektum
     */
    @Override
    public void paint(Graphics g) {
        AppFrame.setGraphicQuality(g);
        if (outPipe != null) {
            AffineTransform at = new AffineTransform();
            at.translate(arrowLocX, arrowLocY);
            at.rotate(Math.toRadians(-arrowAngle));
            at.translate(-arrowSprite.getWidth()/2, -arrowSprite.getHeight()/2);
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(arrowSprite, at, null);
        }
        g.drawImage(sprite, x - sprite.getWidth(null) / 2, y - sprite.getHeight(null)/2, null);
    }

    /**
     * Forrás kirajzolásának és állapotának frissítéséért felelős függvény
     */
    @Override
    public void update() {
        if (spring.getPipes().isEmpty())
            return;
        outPipe = gameView.getPipeViewByCorrespondingModel(spring.getPipes().getFirst());
        if (outPipe == null) {
            return;
        }
        Point pipeDirVector = new Point(outPipe.getEndX() - outPipe.x, outPipe.getEndY() - outPipe.y);
        if (outPipe.getEndX() == x && outPipe.getEndY() == y) {
            pipeDirVector.x *= -1;
            pipeDirVector.y *= -1;
        }
        double xDir = pipeDirVector.x / outPipe.getLength();
        double yDir = pipeDirVector.y / outPipe.getLength();
        arrowLocX = (int) (xDir * r * 1.1 + x);
        arrowLocY = (int) (yDir * r * 1.1 + y);

        double upDirX = 0.0;
        double upDirY = -1.0;
        double dot = Math.acos(upDirX * xDir + upDirY * yDir);
        arrowAngle = Math.toDegrees(dot); //mivel a dir vektor és az upDir vektor hossza is 1

        //ha x pozitív akkor a kiegészítő szögre van szükségünk
        if (xDir >= 0)
            arrowAngle = 360 - arrowAngle;

    }

    /**
     * Megnézi, hogy az egérkattintás a forrásra esett-e
     * @param e - egérkattintás
     * @return - igaz, ha a forrásra esett az egérkattintás, hamis, ha nem
     */
    @Override
    public boolean isIn(MouseEvent e) {
        return Math.sqrt(Math.pow(e.getX() - x, 2) + Math.pow((e.getY() - y), 2)) < r;
    }
    @Override
    public void clickAction(MouseEvent e) {
        if (gameView.cisternDisplay != null)
            gameView.removeCisternDisplay();
        createPopUpBar(x, y, 100, gameView, this);
    }

    @Override
    public Object getCorrespondingModelElement() {
        return spring;
    }

    @Override
    public Point getDefaultPlayerPosition() {
        return new Point(x, y - r - 20);
    }
    @Override
    public Point getRotationCenter() {
        //meg kéne nézni hány játékos van rajta

        Point ret = new Point(x, y);
        return ret;
    }

    @Override
    public double getPlayerAngle(Player p) {
        double[] angles = new double[spring.getPlayers().size()];
        for (int i = 0; i < angles.length; ++i) {
            angles[i] = i * 20 - (angles.length - 1) * 10;
        }
        return angles[spring.getPlayers().indexOf(p)];
    }

    @Override
    public Steppable getCorrespondingModelSteppable() {
        return spring;
    }

    @Override
    public boolean isDraggable() {
        return true;
    }

}
