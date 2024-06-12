package View;

import Model.Player;
import Model.Pump;
import Model.Steppable;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import static View.Pictures.*;
import static java.awt.Color.BLACK;

public class PumpView extends Drawable implements Clickable, CreatePopUpBar, SteppableView {

    public PumpView(int x, int y, int r, Pump p, CisternView c, Window v) {
        super(x, y, v);
        this.r = r;
        pump = p;
        gen = c;

        arrowSprite = pumpIndicatorImg;
        arrowSprite = ImageUtility.scaleImage(arrowSprite, 20);

        if (WindowOptions.windowOption == WindowOptions.game)
            gameView = (GameView) v;
    }
    private GameView gameView;
    private Pump pump;
    private boolean generated;
    private CisternView gen;
    private int r;
    private PipeView outPipe = null;

    private BufferedImage arrowSprite = null;
    private int arrowLocX;
    private int arrowLocY;
    private double arrowAngle;


    @Override
    public void paint(Graphics g) {
        AppFrame.setGraphicQuality(g);
        Color color=new Color(0,0,0);
        if(pump.isBroken())
            color=new Color(250,100,100);
        else {
            if(pump.getHeldWater()<=pump.getWaterCapacity()*0.25 && pump.getHeldWater()!=0)
                color=new Color(0,0,150);
            if(pump.getHeldWater()<=pump.getWaterCapacity()*0.5 && pump.getHeldWater()>=pump.getWaterCapacity()*0.25)
                color=new Color(0,0,200);
            if(pump.getHeldWater()<=pump.getWaterCapacity()*0.75 && pump.getHeldWater()>=pump.getWaterCapacity()*0.5)
                color=new Color(20,20,255);
            if(pump.getHeldWater()<=pump.getWaterCapacity() && pump.getHeldWater()>=pump.getWaterCapacity()*0.75)
                color=new Color(60,170,255);
        }
        if (outPipe != null) {
            AffineTransform at = new AffineTransform();
            at.translate(arrowLocX, arrowLocY);
            at.rotate(Math.toRadians(-arrowAngle));
            at.translate(-arrowSprite.getWidth() / 2, -arrowSprite.getHeight() / 2);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.drawImage(arrowSprite, at, null);
            g2.setTransform(new AffineTransform());
        }

        int topLeftX = x - r;
        int topLeftY = y - r;
        int diameter = 2 * r;
        g.setColor(color);
        g.fillOval(topLeftX - 5, topLeftY - 5, diameter + 10, diameter + 10);
        g.setColor(Color.WHITE);
        g.fillOval(topLeftX, topLeftY, diameter, diameter);
        g.setColor(BLACK);
        g.setFont(new Font("Inter", Font.BOLD, 15));
        g.drawString(Integer.toString(pump.getHeldWater()), x - 20, y+5);
        g.drawString("/" , x - 2, y+5);
        g.drawString( Integer.toString(pump.getWaterCapacity()), x + 5, y+5);
    }

    @Override
    public void paintOnPlayer(int x,int y, Graphics2D graphics2D){
        graphics2D.setColor(BLACK);
        graphics2D.setStroke(new BasicStroke(2));
        int topLeftX = x - 5 + 20;
        int topLeftY = y - 5;
        int diameter = 2 * 5;

        graphics2D.setColor(Color.BLACK);
        graphics2D.fillOval(topLeftX - 5, topLeftY - 5, diameter + 10, diameter + 10);
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillOval(topLeftX, topLeftY, diameter, diameter);
    }
    @Override
    public void update() {
        outPipe = gameView.getPipeViewByCorrespondingModel(pump.getActiveOut());
        if (outPipe == null)
            return;
        Point pipeDirVector = new Point(outPipe.getEndX() - outPipe.x, outPipe.getEndY() - outPipe.y);
        if (outPipe.getEndX() == x && outPipe.getEndY() == y) {
            pipeDirVector.x *= -1;
            pipeDirVector.y *= -1;
        }
        double xDir = pipeDirVector.x / outPipe.getLength();
        double yDir = pipeDirVector.y / outPipe.getLength();
        arrowLocX = (int) (xDir * r * 1.3 + x);
        arrowLocY = (int) (yDir * r * 1.3 + y);

        double upDirX = 0.0;
        double upDirY = -1.0;
        double dot = Math.acos(upDirX * xDir + upDirY * yDir);
        arrowAngle = Math.toDegrees(dot); //mivel a dir vektor és az upDir vektor hossza is 1

        //ha x pozitív akkor a kiegészítő szögre van szükségünk
        if (xDir >= 0)
            arrowAngle = 360 - arrowAngle;

    }

    @Override
    public boolean isIn(MouseEvent e) {
        if (Math.sqrt(Math.pow(e.getX() - x, 2) + Math.pow((e.getY() - y), 2)) < r) {
            return true;
        }
        return false;
    }

    @Override
    public void clickAction(MouseEvent e) {
        createPopUpBar(x, y, 100, gameView, this);
    }

    @Override
    public Object getCorrespondingModelElement() {
        return pump;
    }

    @Override
    public Steppable getCorrespondingModelSteppable() {
        return pump;
    }

    /**
     * Visszaadja, hogy egy új játékost melyik pontban lehet kirajzolni
     *
     * @return
     */
    @Override
    public Point getDefaultPlayerPosition() {
        return new Point(x, y - r - 15);
    }
    @Override
    public Point getRotationCenter() {
        //meg kéne nézni hány játékos van rajta

        Point ret = new Point(x, y);
        return ret;
    }

    @Override
    public double getPlayerAngle(Player p) {
        double[] angles = new double[pump.getPlayers().size()];
        for (int i = 0; i < angles.length; ++i) {
            angles[i] = i * 20 - (angles.length - 1) * 10;
        }
        return angles[pump.getPlayers().indexOf(p)];
    }

    @Override
    public boolean isDraggable() {
        return true;
    }
}
