package gui;

import models.Player;
import javax.swing.*;
import java.awt.*;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URISyntaxException;

/**
 * This is a JFrame which lets users read the rules in PDF before initialize the game
 */
public class PreGameGUI extends JFrame {

    // The PDF route must be relative to the classpath of the aplication.
    private final String PDF_PATH = "/resources/officialRules.pdf";

    public PreGameGUI() {
        // --- Window configuration ---
        setTitle("Rainbow Roulette: Iniciar Juego");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centers the window
        setResizable(false);

        // --- Content of the window ---
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Títle
        JLabel titleLabel = new JLabel("RAINBOW ROULETTE 2-BALLS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Instrucctions
        JLabel instructionLabel = new JLabel("Press 'See rules or Play now'.");
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button panels
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // See rules button
        JButton btnViewRules = new JButton("See rules");
        btnViewRules.addActionListener(e -> openPDFRules());

        // Play button
        JButton btnStartGame = new JButton("¡Jugar Ahora!");
        btnStartGame.addActionListener(e -> startGame());

        buttonPanel.add(btnViewRules);
        buttonPanel.add(btnStartGame);

        // Adding components
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(instructionLabel);
        mainPanel.add(Box.createVerticalStrut(40));
        mainPanel.add(buttonPanel);

        add(mainPanel);
    }

    /** Opens the PDF file using the standard visor from OS. */
    private void openPDFRules() {
        try {
            if (Desktop.isDesktopSupported()) {
                // Obtain the PDF
                URL url = PreGameGUI.class.getResource(PDF_PATH);
                if (url == null) {
                    JOptionPane.showMessageDialog(this,
                            "Error: file was not found 'Reglamento Oficial.pdf' on resources.",
                            "File error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //Open the file. It works on IDE but if the PDF is inside a JAR, previous extraction to a temporarly file
                File pdfFile = new File(url.toURI());
                Desktop.getDesktop().open(pdfFile);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Your Operating System cannot stand the opening of the files",
                        "System error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException | IllegalArgumentException | URISyntaxException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al intentar abrir el reglamento.\n" +
                            "Asegúrese de que tiene un visor de PDF instalado.",
                    "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Starts the game and closes the window. */
    private void startGame() {
        this.dispose(); // Cierra la ventana del launcher

        // Lógica de inicio del juego, la misma que estaba en Main.main()
        SwingUtilities.invokeLater(() -> {
            Player mainPlayer = new Player("Loading...");
            GameGUI game = new GameGUI(mainPlayer);
            game.setVisible(true);
            game.startPlayerSession();
        });
    }
}