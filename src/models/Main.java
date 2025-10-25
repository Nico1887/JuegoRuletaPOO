package models; // Or your actual root package name

import gui.GameGUI;
import javax.swing.SwingUtilities;

/**
 * Main class to start the Rainbow Roulette application.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameGUI();
        });
    }
}