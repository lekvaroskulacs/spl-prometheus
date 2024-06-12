package View;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * ImageUtility is a class that contains static methods for loading and scaling images.
 */
public class ImageUtility {
    /**
     * Loads an image from the Assets folder.
     *
     * @param file The name of the file to load.
     * @return The loaded image.
     */
    public static BufferedImage ImageLoad(String file) {
        BufferedImage image = null;
        try {

            String nameOfFile = System.getProperty("user.dir") + File.separator + "src" + File.separator + "Assets" + File.separator + file;
            File in = new File(nameOfFile);

            image = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Scales an image
     *
     * @param img The image to scale.
     * @return The scaled image.
     */
    public static BufferedImage backgroundScale(BufferedImage img) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        Image scaled = img.getScaledInstance(width * 3 / 4, height * 3 / 4, Image.SCALE_SMOOTH);

        BufferedImage result = new BufferedImage(scaled.getWidth(null), scaled.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = result.getGraphics();
        g.drawImage(scaled, 0, 0, null);
        g.dispose();

        return result;
    }

    /**
     * Rescaling an image.
     *
     * @param img   The image to scale.
     * @param scale The scale factor.
     * @return The scaled image.
     */
    public static BufferedImage scaleImage(BufferedImage img, int scale) {
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        int scaling = Math.min(height, width);
        Image scaled = img.getScaledInstance(scale * width / scaling, scale * height / scaling, Image.SCALE_SMOOTH);

        BufferedImage result = new BufferedImage(scaled.getWidth(null), scaled.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = result.getGraphics();
        g.drawImage(scaled, 0, 0, null);
        g.dispose();

        return result;
    }

    /**
     * Centers an image.
     * @param location - center of the image
     * @param i - image
     * @return - center point
     */
    public static Point centerImage(Point location, Image i) {
        return new Point(location.x - i.getWidth(null) / 2, location.y - i.getHeight(null) / 2);
    }


}