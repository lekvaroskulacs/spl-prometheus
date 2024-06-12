package View;

import Controller.Controller;

import java.awt.event.*;

/**
 * Egér eseményeket kezelő osztály, implementálja a MouseListener és MouseMotionListener interfészeket
 */
public class MouseIn implements MouseListener, MouseMotionListener {
    /**
     * Kattintás eseményt kezelő függvény
     * @param e - kattintás
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        switch (WindowOptions.windowOption) {
            case menu -> Controller.getInstance().getMenuView().mouseClicked(e);
            case newgame -> Controller.getInstance().getNewGameView().mouseClicked(e);
            case game -> Controller.getInstance().getGameView().mouseClicked(e);
        }
    }

    /**
     * Egér lenyomás eseményt kezelő függvény
     * @param e - egér esemény
     */
    @Override
    public void mousePressed(MouseEvent e) {
        switch (WindowOptions.windowOption) {
            case menu -> Controller.getInstance().getMenuView().mousePressed(e);
            case newgame -> Controller.getInstance().getNewGameView().mousePressed(e);
            case game -> Controller.getInstance().getGameView().mousePressed(e);
        }
    }

    /**
     * Egér kattintás elengedése
     * @param e egéresemény
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        switch (WindowOptions.windowOption) {
            case menu -> Controller.getInstance().getMenuView().mouseReleased(e);
            case newgame -> Controller.getInstance().getNewGameView().mouseReleased(e);
            case game -> Controller.getInstance().getGameView().mouseReleased(e);
        }
    }

    /**
     * mouseEntered eseményt kezelő függvény
     * @param e egéresemény
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        switch (WindowOptions.windowOption) {
            case menu -> Controller.getInstance().getMenuView().mouseEntered(e);
            case newgame -> Controller.getInstance().getNewGameView().mouseEntered(e);
            case game -> Controller.getInstance().getGameView().mouseEntered(e);
        }
    }

    /**
     * mouseExited eseményt kezelő függvény
     * @param e egéresemény
     */
    @Override
    public void mouseExited(MouseEvent e) {
        switch (WindowOptions.windowOption) {
            case menu -> Controller.getInstance().getMenuView().mouseExited(e);
            case newgame -> Controller.getInstance().getNewGameView().mouseExited(e);
            case game -> Controller.getInstance().getGameView().mouseExited(e);
        }
    }

    /**
     * mouseDragged eseményt kezelő függvény
     * @param e egéresemény
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        switch (WindowOptions.windowOption) {
            case menu -> Controller.getInstance().getMenuView().mouseDragged(e);
            case newgame -> Controller.getInstance().getNewGameView().mouseDragged(e);
            case game -> Controller.getInstance().getGameView().mouseDragged(e);
        }
    }

    /**
     * mouseMoved eseményt kezelő függvény
     * @param e egéresemény
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        switch (WindowOptions.windowOption) {
            case menu -> Controller.getInstance().getMenuView().mouseMoved(e);
            case newgame -> Controller.getInstance().getNewGameView().mouseMoved(e);
            case game -> Controller.getInstance().getGameView().mouseMoved(e);
        }
    }
}
