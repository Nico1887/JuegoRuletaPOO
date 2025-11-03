package models;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * JPanel customized to display a tiled background image read from the classpath resources.
 */
public class FondoPanel extends JPanel {
    private BufferedImage backgroundImage;

    public FondoPanel(String imagePath) {
        // Set the layout to BorderLayout so it can organize the game content
        setLayout(new BorderLayout(10, 10));

        try {
            // Use ClassLoader to find the image resource
            InputStream is = getClass().getResourceAsStream(imagePath);
            if (is != null) {
                backgroundImage = ImageIO.read(is);
                is.close();
            } else {
                System.err.println("ERROR: Could not find resource: " + imagePath);
                setBackground(Color.DARK_GRAY);
            }
        } catch (IOException e) {
            e.printStackTrace();
            setBackground(Color.DARK_GRAY);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            int w = backgroundImage.getWidth();
            int h = backgroundImage.getHeight();
            if (w > 0 && h > 0) {
                // Tile the image across the panel
                for (int x = 0; x < getWidth(); x += w) {
                    for (int y = 0; y < getHeight(); y += h) {
                        g.drawImage(backgroundImage, x, y, this);
                    }
                }
            }
        }
    }
}
