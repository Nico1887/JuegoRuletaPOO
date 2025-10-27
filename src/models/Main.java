package models; // Or your actual root package name

import gui.GameGUI;
import javax.swing.SwingUtilities;

/**
 * Main class to start the Rainbow Roulette application.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Create the Player instance with a temporary name (will be replaced).
            Player mainPlayer = new Player("Loading...");

            // 2. Create the GUI, passing the Player instance.
            GameGUI game = new GameGUI(mainPlayer);
            game.setVisible(true);

            // 3. Start the session to prompt the user for their name.
            game.startPlayerSession();
        });
    }
}