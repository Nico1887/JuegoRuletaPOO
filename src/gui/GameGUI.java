package gui;

import enums.generics.BetType;
import enums.stakes.*;
import exceptions.*;
import models.*;
import models.bets.Bet;
import models.bets.GenericBet;
import logic.PayoutCalculator;
import logic.GameRound;
import logic.SoundManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameGUI extends JFrame {
    // ▶ Attributes ─────────────────────────────────────────────────────────────────────────────────────────────
    private final JButton btnSpin;
    private final JTextField txtStakeAmount;
    private final JTextField txtBetValue;
    private final JComboBox<BetType> cmbBetType;
    private final JTextArea txtResults;
    private final JLabel lblBalance;
    private final Board board;
    private final Player player;
    private final GameRound gameRound;

    private final JLabel playerNameLabel;

    // ▶ Constructors ───────────────────────────────────────────────────────────────────────────────────────────

    /**
     * Initializes the Game GUI, receiving the Player instance created in Main.
     */
    public GameGUI(Player player) {
        this.player = player;
        Table gameTable = new Table();
        PayoutCalculator payoutCalculator = new PayoutCalculator();
        this.gameRound = new GameRound(gameTable, payoutCalculator);
        this.board = new Board(gameTable);

        final String BACKGROUND_IMAGE_PATH = "/resources/img.png";

        setTitle("Rainbow Roulette 2-Balls");
        setSize(1300, 950);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FondoPanel contentPanel = new FondoPanel(BACKGROUND_IMAGE_PATH);

        SoundManager.playAmbientLoop();

        // 1. Initialize control components
        lblBalance = new JLabel("Balance: $" + String.format("%.2f", player.getBalance()));
        lblBalance.setForeground(java.awt.Color.WHITE);
        txtStakeAmount = new JTextField("100", 5);
        txtBetValue = new JTextField("Rojo", 15);
        cmbBetType = new JComboBox<>(BetType.values());
        txtResults = new JTextArea(15, 30);
        txtResults.setEditable(false);
        btnSpin = new JButton("STAKE AND SPIN!");

        // 2. Initialize player name component
        playerNameLabel = new JLabel("Player: " + player.getUsername());
        playerNameLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        playerNameLabel.setForeground(Color.WHITE);

        // 3. Setup Layout
        JPanel pnlHeader = createHeaderPanel();

        JPanel pnlBoard = new JPanel(new BorderLayout());
        pnlBoard.setOpaque(false);
        pnlBoard.add(board, BorderLayout.CENTER);

        JPanel pnlControls = createControlsPanel();
        pnlControls.setOpaque(false);

        contentPanel.add(pnlHeader, BorderLayout.NORTH);
        contentPanel.add(pnlBoard, BorderLayout.CENTER);
        contentPanel.add(pnlControls, BorderLayout.EAST);

        add(contentPanel);
        btnSpin.addActionListener(e -> processStakeAndSpin());
        setLocationRelativeTo(null);
    }

    /** Creates the top panel for displaying player info. */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        playerNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        lblBalance.setHorizontalAlignment(SwingConstants.RIGHT);
        lblBalance.setFont(new Font("Arial", Font.BOLD, 16));

        headerPanel.add(playerNameLabel, BorderLayout.WEST);
        headerPanel.add(lblBalance, BorderLayout.EAST);

        return headerPanel;
    }

    /** Creates the vertical panel for controls. */
    private JPanel createControlsPanel() {
        JPanel pnlControls = new JPanel(new BorderLayout(10, 10));
        pnlControls.setOpaque(false);
        pnlControls.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JPanel pnlInputFields = new JPanel();
        pnlInputFields.setOpaque(false);
        pnlInputFields.setLayout(new BoxLayout(pnlInputFields, BoxLayout.Y_AXIS));
        pnlInputFields.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));


        pnlInputFields.add(Box.createVerticalStrut(20));


        // Controls Grid
        JPanel pnlGrid = new JPanel(new GridLayout(3, 2, 5, 5));
        pnlGrid.setOpaque(false);

        Font labelFont = new Font("Arial", Font.PLAIN, 12);

        // Row 1: Type
        pnlGrid.add(createLabel("Type:", labelFont));
        pnlGrid.add(cmbBetType);

        // Row 2: Stake
        pnlGrid.add(createLabel("Stake: $", labelFont));
        pnlGrid.add(txtStakeAmount);

        // Row 3: Value
        pnlGrid.add(createLabel("Value (e.g., Rojo):", labelFont));
        pnlGrid.add(txtBetValue);

        pnlInputFields.add(pnlGrid);
        pnlInputFields.add(Box.createVerticalStrut(20));


        // Spin Button
        JPanel pnlButton = createRowPanel();
        pnlButton.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlButton.add(btnSpin);
        pnlInputFields.add(pnlButton);

        pnlControls.add(pnlInputFields, BorderLayout.NORTH);
        pnlControls.add(Box.createVerticalGlue(), BorderLayout.CENTER);


        // History and Results
        JPanel pnlHistory = new JPanel(new BorderLayout(0, 5));
        pnlHistory.setOpaque(false);
        JLabel lblHistory = createLabel("History and Results:", labelFont);

        pnlHistory.add(lblHistory, BorderLayout.NORTH);
        pnlHistory.add(new JScrollPane(txtResults), BorderLayout.CENTER);

        pnlControls.add(pnlHistory, BorderLayout.SOUTH);

        return pnlControls;
    }

    private JPanel createRowPanel() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, p.getPreferredSize().height));
        return p;
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setForeground(java.awt.Color.WHITE);
        label.setFont(font);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }


    /** Creates the specific Stake object based on user input. */
    private Object createStakeInstance(BetType type, String textValue) throws InvalidBetTypeException {
        if (textValue.isEmpty()) {
            if (type == BetType.LUCKY_BLACK) return new LuckyBlackStake();
            throw new InvalidBetTypeException();
        }

        String normalizedValue = textValue.trim().toUpperCase();

        switch (type) {
            case NUMBER:
                try {
                    return new NumberStake(Integer.parseInt(normalizedValue));
                }
                catch (Exception e) { throw new InvalidBetTypeException(); }

            case COLOR:
            case SINGLE_COLOR:
                // Translation for color stakes (resolves 'Rojo' error)
                if (normalizedValue.equals("ROJO")) normalizedValue = "RED";
                else if (normalizedValue.equals("VERDE")) normalizedValue = "GREEN";
                else if (normalizedValue.equals("AZUL")) normalizedValue = "BLUE";
                else if (normalizedValue.equals("NARANJA")) normalizedValue = "ORANGE";
                else if (normalizedValue.equals("VIOLETA")) normalizedValue = "VIOLET";
                else if (normalizedValue.equals("AMARILLO")) normalizedValue = "YELLOW";

                try {
                    return ColorStake.valueOf(normalizedValue);
                }
                catch (Exception e) {
                    throw new InvalidBetTypeException();
                }

            case PARITY:
                if (normalizedValue.equals("PAR")) normalizedValue = "EVEN";
                else if (normalizedValue.equals("IMPAR")) normalizedValue = "ODD";

                try {
                    return ParityStake.valueOf(normalizedValue);
                }
                catch (Exception e) { throw new InvalidBetTypeException(); }

            default:
                throw new InvalidBetTypeException();
        }
    }

    /** Handles the button click: processes bet, starts spin, and sets timer for result. */
    private void processStakeAndSpin() {
        btnSpin.setEnabled(false);
        try {
            double stakeAmount = Double.parseDouble(txtStakeAmount.getText().trim());
            BetType betType = (BetType) cmbBetType.getSelectedItem();
            Object stakeValue = createStakeInstance(betType, txtBetValue.getText().trim());

            Bet bet = new GenericBet<>(player, betType, stakeValue, stakeAmount);

            if (stakeAmount <= 0 || stakeAmount > player.getBalance()) {
                throw new InsufficientBalanceException("Invalid stake amount or insufficient balance.");
            }

            player.deductBalance(stakeAmount);
            player.addBet(bet);

            updateBalance();

            SoundManager.playSpinStart();

            board.startSpin();

            Timer checkTimer = new Timer(500, null);
            checkTimer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (board.isWheelStopped() && board.isBall1Stopped() && board.isBall2Stopped()) {
                        checkTimer.stop();
                        SoundManager.stopSpin();
                        processSpinFinished(board.getResults());
                    }
                }
            });
            checkTimer.start();

        } catch (NumberFormatException | InvalidBetTypeException | NullStakeException ex) {
            showMessage("Input Error", "Input error: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            SoundManager.playLose();
            btnSpin.setEnabled(true);
        } catch (InsufficientBalanceException ex) {
            showMessage("Balance Error", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            SoundManager.playLose();
            btnSpin.setEnabled(true);
        }
    }

    /** Called when the animation stops to settle bets and display results. */
    private void processSpinFinished(Pocket[] results) {
        // Retrieve the last bet BEFORE the game round potentially clears the list
        Bet lastBet = player.getCurrentBets().isEmpty() ? null : player.getCurrentBets().get(0);

        try {
            // Settle bets and add winnings to the player's balance
            gameRound.settleBets(player, results);

            // Get total accumulated winnings (this value should reflect the result after settlement)
            double netWinnings = player.getWinningsThisRound();
            double betAmount = lastBet != null ? lastBet.getAmount() : 0;

            if (lastBet != null) {
                // Sound condition: Play WIN only if there is a net gain.
                if (netWinnings > 0) {
                    SoundManager.playWin();
                } else {
                    SoundManager.playLose();
                }

                // Append detailed result message.
                // Note: netWinnings is passed here, but named 'roundResultAmount' in the method
                txtResults.append(generateResultMessage(lastBet, results, netWinnings) + "\n");
            } else {
                // If the user spun without betting
                txtResults.append("Spin: " + results[0].getNumber() + " | " + results[1].getNumber() + " (No Bet).\n");
                SoundManager.playLose();
            }

            updateBalance();

        } catch (Exception ex) {
            showMessage("Calculation Error", "Error processing results: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        } finally {
            // Ensure the UI is updated and button is re-enabled
            SwingUtilities.invokeLater(() -> btnSpin.setEnabled(true));
        }
    }

    // ▶ New Public Method ──────────────────────────────────────────────────────────────────────────────────────

    /** Displays input dialogs to get the player's name and handles the welcome message. */
    public void startPlayerSession() {
        // 1. Prompt for player name
        String inputName = JOptionPane.showInputDialog(
                this,
                "Please enter your name:",
                "Login",
                JOptionPane.PLAIN_MESSAGE
        );

        String finalName;
        if (inputName != null && !inputName.trim().isEmpty()) {
            finalName = inputName.trim();
        } else {
            finalName = "Guest";
        }

        // 2. Set the name using the Player's setter
        player.setUsername(finalName);

        // 3. Display welcome message
        JOptionPane.showMessageDialog(
                this,
                "Welcome, " + player.getUsername() + "!",
                "Welcome Message",
                JOptionPane.INFORMATION_MESSAGE
        );

        // 4. Update the name label in the GUI
        updatePlayerNameDisplay();
    }

    // ▶ Auxiliary Methods ──────────────────────────────────────────────────────────────────────────────────────
    private void updateBalance() {
        lblBalance.setText("Balance: $" + String.format("%.2f", player.getBalance()));
    }

    private void updatePlayerNameDisplay() {
        playerNameLabel.setText("Player: " + player.getUsername());
    }

    /** Generates a detailed result message including the spin outcome and the bet details.
     * The logic is adjusted to define any result >= 0 as a WIN (or break-even).
     */
    private String generateResultMessage(Bet bet, Pocket[] results, double roundResultAmount) {
        // Usamos un pequeño umbral (epsilon) para manejar errores de coma flotante.
        final double EPSILON = 0.001;

        // 1. Spin Outcome
        String msg = "Spin: " + results[0].getNumber() + " | " + results[1].getNumber() + ". ";

        // 2. Bet Details (Type, Value, Stake)
        String betType = bet.getType().toString();

        Object stakeObject = bet.getStake();
        String betValue = stakeObject.toString();

        if (stakeObject instanceof ColorStake || stakeObject instanceof ParityStake) {
            betValue = betValue.substring(0, 1) + betValue.substring(1).toLowerCase();
        }

        String betDetail = String.format("[%s on %s $%.0f]",
                betType,
                betValue,
                bet.getAmount()
        );

        // 3. Result (Win/Loss/Break-even)
        String resultStatus;

        // Se considera WON/Break-even si la ganancia neta es mayor o igual a cero.
        if (roundResultAmount > EPSILON) {
            // WIN: Ganancia neta positiva
            resultStatus = String.format("WON! Net: $%.2f.", roundResultAmount);
        } else if (Math.abs(roundResultAmount) <= EPSILON) {
            // BREAK-EVEN: Ganancia neta muy cercana a cero (la apuesta fue devuelta)
            // Esto es necesario para que el balance de 1100 (ganancia de 100) no sea clasificado aquí.
            resultStatus = "Break-even (Stake returned).";
        } else {
            // LOST: Ganancia neta negativa
            resultStatus = "LOST!";
        }

        return msg + betDetail + " " + resultStatus;
    }

    private void showMessage(String title, String message, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }
}