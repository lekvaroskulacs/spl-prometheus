package View;

import Controller.Controller;
import Model.Mechanic;
import Model.Player;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static View.Pictures.*;

/**
 * Mechannic kiiratásáért felelős osztály
 */
public class MechanicView extends Drawable {
    /**
     * Konstruktor
     *
     * @param x x koordináta
     * @param y y koordináta
     * @param m Mechanic
     * @param v Window
     */
    public MechanicView(int x, int y, Mechanic m, Window v) {
        super(x, y, v);
        mechanic = m;
        if (WindowOptions.windowOption == WindowOptions.game)
            gameView = (GameView) v;
        color = Color.BLUE;

    }

    private GameView gameView;
    private Mechanic mechanic;
    private Color color;
    private double angle;
    private Point rotationCenter = new Point(0, 0);

    /**
     * Szín beállítása
     *
     * @param color beállítandó szín
     */
    public void setColor(Color color) {
        this.color = color;
    }

    Image imageForScale = ImageUtility.scaleImage(mapPrevButton, 200);

    private int number;

    /**
     * Szám beállítása
     *
     * @param number beállítandó szám
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Kirajzolás
     *
     * @param g - a kirajzolásért felelős Graphics objektum
     */
    @Override
    public void paint(Graphics g) {
        // ha a játék ablakban vagyunk, akkor a játékos színével rajzoljuk ki
        if (WindowOptions.windowOption == WindowOptions.newgame) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = (int) screenSize.getWidth() * 3 / 4;
            int screenHeight = (int) screenSize.getHeight() * 3 / 4;
            AppFrame.setGraphicQuality(g);
            Graphics2D graphics2D = (Graphics2D) g.create();
            graphics2D.setColor(color);
            graphics2D.setFont(new Font("Inter", Font.BOLD, 30));
            graphics2D.drawString("" + color.getRed() + "/" + color.getGreen() + "/" + color.getBlue(), 50 + imageForScale.getWidth(null) + 100, screenHeight / 2 + 20 + number * 50);

        }
        // ha a mechanic még nincs eltávolítva
        if (!mechanic.isRemoved()) {
            AppFrame.setGraphicQuality(g);
            int[] pointsX = new int[3];
            int[] pointsY = new int[3];
            pointsX[0] = x;
            pointsY[0] = y + 12 + 1;
            pointsX[1] = x - 8 + 1;
            pointsY[1] = y - 10 - 1;
            pointsX[2] = x + 8 + 1;
            pointsY[2] = y - 10 - 1;

            int[] capX = new int[3];
            int[] capY = new int[3];
            capX[0] = x;
            capY[0] = y - 22;
            capX[1] = x - 8;
            capY[1] = y - 15;
            capX[2] = x + 8;
            capY[2] = y - 15;

            g.setColor(color);
            Graphics2D graphics2D = (Graphics2D) g.create();
            AffineTransform at = new AffineTransform();
            at.rotate(Math.toRadians(angle), rotationCenter.x, rotationCenter.y);
            if (!mechanic.isFellDown())
                graphics2D.transform(at);
            graphics2D.fillPolygon(pointsX, pointsY, 3);
            if (Controller.getInstance().getActivePlayer() == mechanic)
                graphics2D.fillPolygon(capX, capY, 3);
            if (mechanic.isStuck())
                graphics2D.fillRect(x - 7, y - 25, 16, 10);
            if (mechanic.getHeldItems() != null)
                Controller.getInstance().getGameView().getDrawableByCorrespondingModel(mechanic.getHeldItems()).paintOnPlayer(x, y, graphics2D);

            graphics2D.setTransform(new AffineTransform());
        }
    }

    /**
     * Frissítés
     */
    @Override
    public void update() {
        // ha leeseett a mechanic
        if (mechanic.isFellDown()) {
            int i = Player.fallen.indexOf(mechanic);
            x = Controller.getInstance().getFrame().getWidth() - 50 - 30 * i;
            y = Controller.getInstance().getFrame().getHeight() - 50;
            return;
        }
        SteppableView standingOn = Controller.getInstance().getGameView().getSteppableViewByCorrespondingModel(mechanic.getStandingOn());
        Point p = standingOn.getDefaultPlayerPosition();
        x = p.x;
        y = p.y;
        angle = standingOn.getPlayerAngle(mechanic);
        rotationCenter = standingOn.getRotationCenter();
    }

    @Override
    public Object getCorrespondingModelElement() {
        return mechanic;
    }

    /**
     * Mechanic x koordinátájának lekérdezése
     *
     * @return x koordináta
     */
    public int getX() {
        return x;
    }

    /**
     * Mechanic y koordinátájának lekérdezése
     *
     * @return y koordináta
     */
    public int getY() {
        return y;
    }

}
