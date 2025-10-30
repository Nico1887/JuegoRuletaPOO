package models; // Or your actual root package name

import gui.GameGUI;
import gui.PreGameGUI;

import javax.swing.SwingUtilities;

/**
 * Main class to start the Rainbow Roulette application.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initializes and shows the pregame window.
            PreGameGUI launcher = new PreGameGUI();
            launcher.setVisible(true);

            //The logic to create a Player, GameGui and start the session
            //was moved to PreGameGUI.startGame()
        });
    }
}