package View;

import Controller.Controller;

import javax.swing.*;
import java.awt.*;

/**
 * A játék ablaka, leszármazottja a JFrame-nek
 */
public class AppFrame extends JFrame {

    /**
     * Konstruktor, beállítja a paramétereket
     * @param panel - a panel, amit megjelenít
     */
    public AppFrame(AppPanel panel) {
        super("Drukmákori Délibáb");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        setSize(width * 3/4, height * 3/4);
        add(panel);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //tesztelés
        WindowOptions.windowOption = WindowOptions.menu;
    }

    /**
     * graphics quality beállítása
     * @param g - a kirajzolásért felelős Graphics objektum
     */
    public static void setGraphicQuality(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }


}
