package View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * A játék ablakát reprezentáló osztály
 */
public abstract class Window {
    protected LinkedList<Drawable> drawables = new LinkedList<>();
    protected LinkedList<Clickable> clickables = new LinkedList<>();
    protected LinkedList<JComponent> components = new LinkedList<>();

    public abstract void paint(Graphics g);
    public abstract void update();
    public abstract void mousePressed(MouseEvent e);
    public abstract void mouseReleased(MouseEvent e);
    public abstract void mouseEntered(MouseEvent e);
    public abstract void mouseExited(MouseEvent e);
    public abstract void mouseDragged(MouseEvent e);
    public abstract void mouseMoved(MouseEvent e);
    public abstract void mouseClicked(MouseEvent e);

    public Drawable getDrawableByCorrespondingModel(Object o) {
        for (Drawable d : drawables) {
            if (d.getCorrespondingModelElement() == o)
                return d;
        }
        return null;
    }

    public void addDrawable(Drawable d, boolean highDrawPriority) {
        if (highDrawPriority) {
            drawables.add(d);
        }
        else
            drawables.addFirst(d);
    }

    public void addClickable(Clickable c, boolean highClickPriority) {
        if (highClickPriority) {
            clickables.addFirst(c);
        } else
            clickables.add(c);
    }

    public void addComponent(JComponent component) {
        components.add(component);
    }

    public void remove(Object o) {
        drawables.remove(o);
        clickables.remove(o);
    }

}
