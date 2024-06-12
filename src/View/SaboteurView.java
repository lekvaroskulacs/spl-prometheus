package View;

import Model.Player;
import Model.Saboteur;
import Controller.Controller;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import static View.Pictures.mapPrevButton;

public class SaboteurView extends Drawable {


    public SaboteurView(int x, int y, Saboteur s, Window v) {
        super(x, y, v);
        saboteur = s;
        if (WindowOptions.windowOption == WindowOptions.game)
            gameView = (GameView) v;
        color = Color.RED;
    }
    private GameView gameView;
    private Color color;
    private Saboteur saboteur;
    private double angle;
    private Point rotationCenter = new Point(0, 0);

    public void setColor(Color color) {
        this.color = color;
    }
    Image imageForScale=ImageUtility.scaleImage(mapPrevButton,200);
    private  int number;

    public void setNumber(int number) {
        this.number = number;
    }
    @Override
    public void paint(Graphics g) {
        if(WindowOptions.windowOption==WindowOptions.newgame)
        {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = (int) screenSize.getWidth() * 3 / 4;
            int screenHeight = (int) screenSize.getHeight() * 3 / 4;
            AppFrame.setGraphicQuality(g);
            Graphics2D graphics2D = (Graphics2D) g.create();
            graphics2D.setColor(color);
            graphics2D.setFont(new Font("Inter",Font.BOLD,30));
            graphics2D.drawString(""+color.getRed()+"/"+color.getGreen()+"/"+color.getBlue(),50+imageForScale.getWidth(null)+100+220, screenHeight/2+20+number*50);
        }
        if (!saboteur.isRemoved()) {
            AppFrame.setGraphicQuality(g);
            int[] pointsX = new int[3];
            int[] pointsY = new int[3];
            pointsX[0] = x;
            pointsY[0] = y + 10;
            pointsX[1] = x - 6;
            pointsY[1] = y - 8;
            pointsX[2] = x + 6;
            pointsY[2] = y - 8;
            int[] capX = new int[3];
            int[] capY = new int[3];
            capX[0] = x;
            capY[0] = y - 22;
            capX[1] = x - 8;
            capY[1] = y - 15;
            capX[2] = x + 8;
            capY[2] = y - 15;
            Graphics2D graphics2D = (Graphics2D) g.create();
            graphics2D.setStroke(new BasicStroke(3));
            AffineTransform at = new AffineTransform();
            at.rotate(Math.toRadians(angle), rotationCenter.x, rotationCenter.y);
            if (!saboteur.isFellDown()) {
                graphics2D.transform(at);
            }
            graphics2D.setColor(color);
            graphics2D.drawPolygon(pointsX, pointsY, 3);
            if (Controller.getInstance().getActivePlayer() == saboteur)
                graphics2D.fillPolygon(capX, capY, 3);
            if (saboteur.isStuck())
                graphics2D.fillRect(x - 7, y - 25, 16, 10);
            graphics2D.setTransform(new AffineTransform());
            graphics2D.setStroke(new BasicStroke(1));
        }
    }

    @Override
    public void update() {
        if (saboteur.isFellDown()) {
            int i = Player.fallen.indexOf(saboteur);
            x = Controller.getInstance().getFrame().getWidth() - 50 - 30 * i;
            y = Controller.getInstance().getFrame().getHeight() - 50;
            return;
        }
        SteppableView standingOn = Controller.getInstance().getGameView().getSteppableViewByCorrespondingModel(saboteur.getStandingOn());
        Point p = standingOn.getDefaultPlayerPosition();
        x = p.x;
        y = p.y;
        angle = standingOn.getPlayerAngle(saboteur);
        rotationCenter = standingOn.getRotationCenter();
    }

    @Override
    public Object getCorrespondingModelElement() {
        return saboteur;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


}
