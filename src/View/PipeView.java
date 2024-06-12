package View;

import Model.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import static java.awt.Color.*;

/**
 * Cső kirajzolásáért felelős osztály, megvalósítja a Drawable, Clickable, CreatePopUpBar, SteppableView interfészeket
 */
public class PipeView extends Drawable implements Clickable, CreatePopUpBar, SteppableView {

    /**
     * Konstruktor
     * @param startNode kezdőpont
     * @param endNode végpont
     * @param width szélessége
     * @param p cső
     * @param v ablak
     */
    public PipeView(Drawable startNode, Drawable endNode, int width, Pipe p, Window v) {
        super(startNode.x, startNode.y,v);
        this.endX = endNode.x;
        this.endY = endNode.y;
        this.width = width;
        this.startNode = startNode;
        this.endNode = endNode;
        pipe = p;

        if (WindowOptions.windowOption == WindowOptions.game)
            gameView = (GameView) v;
    }
    public boolean selected;
    private GameView gameView;
    private Pipe pipe;
    private int endX;
    private int endY;
    private int width;
    private Drawable startNode = null;
    private Drawable endNode = null;

    /**
     * Cső kirajzolása
     * @param g - a kirajzolásért felelős Graphics objektum
     */
    @Override
    public void paint(Graphics g) {
        AppFrame.setGraphicQuality(g);
        Graphics2D graphics2D = (Graphics2D) g;

        Point pipeDir = new Point(endX - x, endY - y);
        Point pipeNormal = new Point(pipeDir.y, -pipeDir.x);
        double unitNormX = pipeNormal.x / getLength();
        double unitNormY = pipeNormal.y / getLength();
        int x1 = (int) Math.ceil(unitNormX * width *1.4) + x;
        int y1 = (int) Math.ceil(unitNormY * width *1.4) + y;
        int x2 = (int) Math.ceil(-unitNormX * width *1.4) + x;;
        int y2 = (int) Math.ceil(-unitNormY * width *1.4) + y;
        int x3 = (int) Math.ceil(unitNormX * width *1.4) + endX;
        int y3 = (int) Math.ceil(unitNormY * width *1.4) + endY;
        int x4 = (int) Math.ceil(-unitNormX * width *1.4) + endX;
        int y4 = (int) Math.ceil(-unitNormY * width *1.4) + endY;

        Color mainColor = BLACK;
        Color brokenColor = RED;
        Color miscColor = CYAN;
        boolean drawBroken = false;
        boolean drawMisc = false;

        if (pipe.isBeingHeld()) {
            Stroke dashed = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
            graphics2D.setStroke(dashed);
            graphics2D.setColor(LIGHT_GRAY);
            graphics2D.drawLine(x,y,endX,endY);
            return;
        } else {
            graphics2D.setStroke(new BasicStroke(width));
        }

        if (pipe.getHeldWater() == 1) {
            mainColor = new Color(47, 178, 245);

            if (pipe.isBroken()) {
                drawBroken = true;
            }

            if (pipe.isGlued()) {
                drawMisc = true;
                miscColor = new Color(61, 153, 3);
            } else if (pipe.isLubricated()) {
                drawMisc = true;
                miscColor = new Color(44, 121, 131);
            }

        }
        else if (
                pipe.getNodes().getFirst().getActiveIn() != pipe &&
                pipe.getNodes().getFirst().getActiveOut() != pipe &&
                pipe.getNodes().getLast().getActiveIn() != pipe &&
                pipe.getNodes().getLast().getActiveOut() != pipe)
        {
            mainColor = LIGHT_GRAY;

            if (pipe.isBroken()) {
                drawBroken = true;
            }

            if (pipe.isGlued()) {
                drawMisc = true;
                miscColor = new Color(61, 153, 3);
            } else if (pipe.isLubricated()) {
                drawMisc = true;
                miscColor = new Color(44, 121, 131);
            }

        } else {

            mainColor = BLACK;

            if (pipe.isBroken()) {
                drawBroken = true;
            }

            if (pipe.isGlued()) {
                drawMisc = true;
                miscColor = new Color(61, 153, 3);
            } else if (pipe.isLubricated()) {
                drawMisc = true;
                miscColor = new Color(44, 121, 131);
            }
        }

        if (drawBroken) {
            graphics2D.setColor(brokenColor);
            graphics2D.drawLine(x1, y1, x3, y3);
        }
        if (drawMisc) {
            graphics2D.setColor(miscColor);
            graphics2D.drawLine(x2, y2, x4, y4);
        }
        graphics2D.setColor(mainColor);
        graphics2D.drawLine(x,y,endX,endY);

        graphics2D.setStroke(new BasicStroke());

        if (selected) {
            graphics2D.fillOval((x + endX) / 2 - 5, (y + endY) / 2 - 5, 10, 10);
        }
    }

    /**
     * Játékos kirajzolása az elemen
     * @param x - x koordináta
     * @param y - y koordináta
     * @param graphics2D - a kirajzolásért felelős Graphics2D objektum
     */
    @Override
    public void paintOnPlayer(int x, int y, Graphics2D graphics2D){
        graphics2D.setColor(BLACK);
        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.drawLine(x-8,y-15,x+8,y-15);
    }

    /**
     * Frissíti a cső kirajzolását
     */
    @Override
    public void update() {
        LinkedList<WaterNode> nodes = pipe.getNodes();
        if (startNode == endNode) {
            for (MechanicView m : gameView.mechanics) {
                if ( ((Mechanic) m.getCorrespondingModelElement()).getHeldItems() == pipe ) {
                    endNode = m;
                }
            }
        }

        if (nodes.size() == 2) {
            startNode = view.getDrawableByCorrespondingModel(nodes.getFirst());
            endNode = view.getDrawableByCorrespondingModel(nodes.getLast());
        }

        if (nodes.size() == 1) {
            for (MechanicView m : gameView.mechanics) {
                if ( ((Mechanic) m.getCorrespondingModelElement()).getHeldItems() == pipe ) {
                    endNode = m;
                }
            }
        }

        if (nodes.isEmpty()) {
            for (MechanicView m : gameView.mechanics) {
                if ( ((Mechanic) m.getCorrespondingModelElement()).getHeldItems() == pipe ) {
                    endNode = m;
                }
            }
            for (MechanicView m : gameView.mechanics) {
                if (
                        ((Mechanic) m.getCorrespondingModelElement()).getHeldItems() == pipe && m != endNode)
                {
                    startNode = m;
                }

            }
        }
        try {
            x = startNode.x;
            y = startNode.y;
            endX = endNode.x;
            endY = endNode.y;
        } catch (NullPointerException e) {
            System.err.println("pipe start or end node is null");
            e.printStackTrace();
        }
    }

    /**
     * Hitbox lekérdezése
     * @param e - egérkattintás
     * @return a hitboxon belülre kattintottunk-e
     */
    @Override
    public boolean isIn(MouseEvent e) {
        //kinda works
        Point pipeDir = new Point(endX - x, endY - y);
        Point pipeNormal = new Point(pipeDir.y, -pipeDir.x);
        double unitNormX = pipeNormal.x / getLength();
        double unitNormY = pipeNormal.y / getLength();
        int[] pointsX = new int[4];
        int[] pointsY = new int[4];
        pointsX[0] = (int) Math.ceil(unitNormX * width * 4) + x;
        pointsY[0] = (int) Math.ceil(unitNormY * width * 4) + y;
        pointsX[1] = (int) Math.ceil(-unitNormX * width * 4) + x;;
        pointsY[1] = (int) Math.ceil(-unitNormY * width * 4) + y;
        pointsX[2] = (int) Math.ceil(-unitNormX * width * 4) + endX;
        pointsY[2] = (int) Math.ceil(-unitNormY * width * 4) + endY;
        pointsX[3] = (int) Math.ceil(unitNormX * width * 4) + endX;
        pointsY[3] = (int) Math.ceil(unitNormY * width * 4) + endY;


        Polygon hitbox = new Polygon(pointsX, pointsY, 4);

        return hitbox.contains(e.getX(), e.getY());
    }

    /**
     * A csőre kattintás eseménye
     * @param e - egérkattintás
     */
    @Override
    public void clickAction(MouseEvent e) {
        if (gameView.cisternDisplay != null)
            gameView.removeCisternDisplay();
        createPopUpBar(e.getX(), e.getY(), 100, view, this);
    }

    /**
     * Visszaadja a csőt reprezentáló modell elemet
     * @return cső
     */
    @Override
    public Object getCorrespondingModelElement() {
        return pipe;
    }

    /**
     * Visszaadja a steppablet reprezentáló modell elemet
     * @return cső
     */
    @Override
    public Steppable getCorrespondingModelSteppable() {
        return pipe;
    }

    /**
     * Visszaadja, hogy egy új játékost melyik pontban lehet kirajzolni
     * @return
     */
    @Override
    public Point getDefaultPlayerPosition() {
        return new Point((x + endX)/2, (y + endY)/2 - 20);
    }

    /**
     * Visszaadja, hogy egy új játékos melyik pont körül kell forgatni
     * @return forgatott pont
     */
    @Override
    public Point getRotationCenter() {
        return new Point((x + endX)/2, (y + endY)/2 - 20);
    }

    /**
     * Visszaadja, hogy egy új játékos milyen szöggel kell kirajzolni
     * @param p - játékos
     * @return szög
     */
    @Override
    public double getPlayerAngle(Player p) {
        return 0;
    }

    /**
     * Visszaadja a cső kezdőpontjának x koordinátáját
     * @return x
     */
    public int getEndX() {
        return endX;
    }

    /**
     * Visszaadja a cső kezdőpontjának y koordinátáját
     * @return y
     */
    public int getEndY() {
        return endY;
    }
    /**
     * Visszaadja a cső hosszát
     * @return hossz
     */
    public double getLength() {
        int vecX = endX - x;
        int vecY = endY - y;
        return Math.sqrt(vecX * vecX + vecY * vecY);
    }
    /**
     * Visszaadja a cső szélességét
     * @return szélesség
     */
    @Override
    public boolean isDraggable() {
        return false;
    }
}
