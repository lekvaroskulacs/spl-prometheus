package View;

import Controller.Controller;

import javax.swing.*;
import java.awt.*;

/**
 * A játék ablaka, leszármazottja a JFrame-nek
 */
public class AppPanel extends JPanel {

    private MouseIn mouseIn=new MouseIn();

    /**
     * Konstruktor, beállítja a paramétereket
     */
    public AppPanel(){
        setFocusable(true);
        requestFocusInWindow();
        addMouseListener(mouseIn);
        addMouseMotionListener(mouseIn);
    }

    /**
     * kirajzolás
     * @param g - a kirajzolásért felelős Graphics objektum
     */
    public void paint(Graphics g){
        super.paintComponent(g);
        Controller.getInstance().paint(g);
    }
}