package View;

import Controller.Controller;
import Controller.Main;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import static View.Pictures.*;
import static View.PopUpButton.*;

/**
 * A játék nézetét megvalósító osztály.
 */
public class GameView extends Window {

    /**
     * Map beállítása.
     *
     * @param file - a beállítandó pálya fájlja.
     */
    public void mapSetup(String file) {
        Main.readFile(file, null);
    }

    Clickable dragged = null;

    protected LinkedList<CisternView> cisterns = new LinkedList<>();
    protected LinkedList<PumpView> pumps = new LinkedList<>();
    protected LinkedList<PipeView> pipes = new LinkedList<>();
    protected LinkedList<SpringView> springs = new LinkedList<>();
    protected LinkedList<SaboteurView> saboteurs = new LinkedList<>();
    protected LinkedList<MechanicView> mechanics = new LinkedList<>();
    protected LinkedList<SteppableView> steppables = new LinkedList<>();
    protected PopUpBar popUpBar = null;
    protected PopUpBar cisternDisplay = null;
    private boolean ended = false;
    private Button exit;


    private AppFrame frame;

    /**
     * Frame beállítása.
     *
     * @param frame - a beállítandó frame.
     */
    public void setFrame(AppFrame frame) {
        this.frame = frame;
    }

    /**
     * Konstruktor.
     *
     * @param frame - a frame, amelyen a játék nézet megjelenik.
     */
    public GameView(AppFrame frame) {
        this.frame = frame;
        exit = new Button(frame.getWidth() - 80, 10, 50, 50, this);
        exit.img = ImageUtility.scaleImage(exitButton, 45);
        exit.option = WindowOptions.exit;
        addDrawable(exit, true);
        addClickable(exit, true);
    }

    protected boolean started = false;

    /**
     * A játék indítását végrehajtó függvény.
     */
    public void setStarted(boolean started) {
        this.started = started;
    }


    private Object sync = new Object();

    /**
     * kirajzolás megvalósítása.
     *
     * @param g - a kirajzolásért felelős Graphics objektum.
     */
    @Override
    public void paint(Graphics g) {
        synchronized (sync) {
            AppFrame.setGraphicQuality(g);
            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.setStroke(new BasicStroke(4));
            if (ended) {
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawRect(Controller.getInstance().getFrame().getWidth() - 1100, -20, 1100, 100);
                graphics2D.setColor(Color.WHITE);
                graphics2D.fillRect(Controller.getInstance().getFrame().getWidth() - 1100 + 2, -20, 1100, 98);
                g.setColor(Color.ORANGE);
                g.setFont(new Font("Inter", Font.BOLD, 50));
                if (PointCounter.getInstance().GetMechanicPoints() > PointCounter.getInstance().GetSaboteurPoints())
                    g.drawString("A SZERELŐK NYERTEK!", Controller.getInstance().getFrame().getWidth() - 1050, 55);
                else if (PointCounter.getInstance().GetMechanicPoints() < PointCounter.getInstance().GetSaboteurPoints())
                    g.drawString("A SZABOTŐRÖK NYERTEK!", Controller.getInstance().getFrame().getWidth() - 1050, 55);
                else if (PointCounter.getInstance().GetMechanicPoints() == PointCounter.getInstance().GetSaboteurPoints())
                    g.drawString("DÖNTETLEN", Controller.getInstance().getFrame().getWidth() - 1050, 55);

            } else {
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawRect(Controller.getInstance().getFrame().getWidth() - 450, -20, 450, 100);
                graphics2D.setColor(Color.WHITE);
                graphics2D.fillRect(Controller.getInstance().getFrame().getWidth() - 450 + 2, -20, 450, 98);
            }
            g.setColor(Color.BLACK);
            g.setFont(new Font("Inter", Font.BOLD, 50));
            g.drawString(Integer.toString(PointCounter.getInstance().GetMechanicPoints()), Controller.getInstance().getFrame().getWidth() - 450 + 50, 55);
            g.drawImage(ImageUtility.scaleImage(scoreDivider, 35), Controller.getInstance().getFrame().getWidth() - 300, 20, null);
            g.drawString(Integer.toString(PointCounter.getInstance().GetSaboteurPoints()), Controller.getInstance().getFrame().getWidth() - 450 + 200, 55);

            for (Drawable d : drawables) {
                d.paint(g);
            }
            for (MechanicView m : mechanics) {
                m.paint(g);
            }
            for (SaboteurView s : saboteurs) {
                s.paint(g);
            }
            for (JComponent c : components) {
                c.paint(g);
            }
            if (popUpBar != null)
                popUpBar.paint(g);

            if (PopUpButton.waitingForParameter) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Inter", Font.BOLD, 20));
                AppFrame f = Controller.getInstance().getFrame();
                g2d.drawString("Válaszd ki a két átirányítandó csövet!", 40, 40);
            }
        }
    }

    /**
     * A játék végét jelző függvény
     */
    public void end() {
        ended = true;
    }

    /**
     * A játék update-ét végrehajtó függvény.
     */
    @Override
    public void update() {
        if (started) {
            synchronized (sync) {
                for (Drawable d : drawables) {
                    d.update();
                }
            }
        }
    }


    @Override
    public void mousePressed(MouseEvent e) {
        synchronized (sync) {
            if (popUpBar != null && !popUpBar.inButtons(e) && !ended)
                removePopUpBar();
            if (SwingUtilities.isRightMouseButton(e))
                return;
            for (Clickable c : clickables) {
                if (c.isDraggable() && c.isIn(e)) {
                    dragged = c;
                    //a drag-elt dolog legyen a legfelső elem amit megjelenítünk
                    if (drawables.contains(c)) {
                        drawables.remove(c);
                        drawables.add((Drawable) c);
                    }
                    clickables.remove(c);
                    clickables.addFirst(c);
                    return;
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        synchronized (sync) {
            dragged = null;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        synchronized (sync) {
            if (dragged != null && drawables.contains(dragged)) {
                Drawable d = (Drawable) dragged;
                d.x = e.getX();
                d.y = e.getY();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        synchronized (sync) {
            if (popUpBar != null)
                return;
            boolean hit = false;
            for (CisternView c : cisterns) {
                if (c.isIn(e)) {
                    if (cisternDisplay == null)
                        c.displayCreated();
                    hit = true;
                }
            }
            if (!hit && cisternDisplay != null)
                removeCisternDisplay();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        synchronized (sync) {
            if (SwingUtilities.isRightMouseButton(e)) {
                for (Clickable c : clickables) {
                    if (c.isIn(e)) {
                        c.clickAction(e);
                        return;
                    }
                }
            } else if (SwingUtilities.isLeftMouseButton(e) && popUpBar != null) {
                for (PopUpButton b : popUpBar.getButtons())
                    if (b.isIn(e)) {
                        b.clickAction(e);
                        return;
                    }
            } else if (SwingUtilities.isLeftMouseButton(e) && waitingForParameter && parameters.size() < 2) {
                for (PipeView p : pipes) {
                    if (p.isIn(e)) {
                        parameters.add(p);
                        p.selected = true;
                        if (parameters.size() == 2)
                            PopUpButton.paramsReceived();
                    }
                }
            } else if (SwingUtilities.isLeftMouseButton(e)) {
                if (exit.isIn(e)) {
                    exit.clickAction(e);
                }
            }
        }
    }

    public SteppableView getSteppableViewByCorrespondingModel(Steppable steppable) {
        for (SteppableView s : steppables) {
            if (s.getCorrespondingModelSteppable() == steppable)
                return s;
        }
        return null;
    }

    public void addPumpView(PumpView p) {
        addDrawable(p, true);
        addClickable(p, true);
        steppables.add(p);
        pumps.add(p);
    }

    public void addPipeView(PipeView p) {
        addDrawable(p, false);
        addClickable(p, false);
        steppables.add(p);
        pipes.add(p);
    }

    public void addSpringView(SpringView s) {
        addDrawable(s, true);
        addClickable(s, true);
        steppables.add(s);
        springs.add(s);
    }

    public void addCisternView(CisternView c) {
        addDrawable(c, true);
        addClickable(c, true);
        steppables.add(c);
        cisterns.add(c);
    }

    public void addMechanicView(MechanicView m) {
        addDrawable(m, true);
        mechanics.add(m);
    }

    public void addSaboteurView(SaboteurView s) {
        addDrawable(s, true);
        saboteurs.add(s);
    }

    public PipeView getPipeViewByCorrespondingModel(Object o) {
        for (PipeView p : pipes) {
            if (p.getCorrespondingModelElement() == o)
                return p;
        }
        return null;
    }

    public void addPopupBar(PopUpBar bar) {
        addDrawable(bar, true);
        popUpBar = bar;
    }

    public void removePopUpBar() {
        drawables.remove(popUpBar);
        popUpBar.delete();
        popUpBar = null;
    }

    public void addCisternDisplay(PopUpBar bar) {
        addDrawable(bar, true);
        cisternDisplay = bar;
    }

    public void removeCisternDisplay() {
        drawables.remove(cisternDisplay);
        cisternDisplay.delete();
        cisternDisplay = null;
    }

    @Override
    public void remove(Object o) {
        super.remove(o);
        cisterns.remove(o);
        pipes.remove(o);
        pumps.remove(o);
        springs.remove(o);
        saboteurs.remove(o);
        mechanics.remove(o);
        steppables.remove(o);
    }

}
