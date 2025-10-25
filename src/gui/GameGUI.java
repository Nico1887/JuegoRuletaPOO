package gui;

import enums.generics.BetType;
import enums.stakes.*;
import exceptions.*;
import models.*;
import models.bets.Bet;
import models.bets.GenericBet;
import logic.PayoutCalculator;
import logic.GameRound;
import javax.swing.*;
import java.awt.*;

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

    // ▶ Constructors ───────────────────────────────────────────────────────────────────────────────────────────
    public GameGUI() {
        this.player = new Player("Player1", 1000.0);
        Table gameTable = new Table();
        PayoutCalculator payoutCalculator = new PayoutCalculator();
        this.gameRound = new GameRound(gameTable, payoutCalculator);
        this.board = new Board(gameTable);

        // --- BACKGROUND IMAGE PATH (Updated to img.png) ---
        final String BACKGROUND_IMAGE_PATH = "/resources/img.png";

        setTitle("Rainbow Roulette 2-Balls");
        setSize(1300, 950);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Uses FondoPanel as the main content container
        FondoPanel contentPanel = new FondoPanel(BACKGROUND_IMAGE_PATH);

        lblBalance = new JLabel("Balance: $" + String.format("%.2f", player.getBalance()));
        lblBalance.setForeground(java.awt.Color.WHITE);
        txtStakeAmount = new JTextField("100", 5);
        txtBetValue = new JTextField("Rojo", 15);
        cmbBetType = new JComboBox<>(BetType.values());
        txtResults = new JTextArea(15, 30);
        txtResults.setEditable(false);
        btnSpin = new JButton("STAKE AND SPIN!");

        updateBalance();

        // --- Setting up Layout ---
        JPanel pnlBoard = new JPanel(new BorderLayout());
        pnlBoard.setOpaque(false); // Transparent to show background
        pnlBoard.add(board, BorderLayout.CENTER);

        JPanel pnlControls = createControlsPanel();
        pnlControls.setOpaque(false); // Transparent

        contentPanel.add(pnlBoard, BorderLayout.CENTER);
        contentPanel.add(pnlControls, BorderLayout.EAST);

        add(contentPanel);
        btnSpin.addActionListener(e -> processStakeAndSpin());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /** Creates the vertical panel for controls, optimized for legibility. */
    private JPanel createControlsPanel() {
        JPanel pnlControls = new JPanel(new BorderLayout(10, 10));
        pnlControls.setOpaque(false); // Ensures background is visible
        pnlControls.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // Panel for input fields
        JPanel pnlInputFields = new JPanel();
        pnlInputFields.setOpaque(false);
        pnlInputFields.setLayout(new BoxLayout(pnlInputFields, BoxLayout.Y_AXIS));
        pnlInputFields.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));


        // 1. BALANCE
        lblBalance.setFont(new Font("Arial", Font.BOLD, 14));
        JPanel pnlBalance = createRowPanel();
        pnlBalance.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlBalance.add(lblBalance);
        pnlInputFields.add(pnlBalance);
        pnlInputFields.add(Box.createVerticalStrut(20));


        // 2. CONTROLS GRID (TYPE, STAKE, VALUE)
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
        pnlGrid.add(createLabel("Value (e.g., Red):", labelFont));
        pnlGrid.add(txtBetValue);

        pnlInputFields.add(pnlGrid);
        pnlInputFields.add(Box.createVerticalStrut(20));


        // 3. SPIN BUTTON (Right aligned)
        JPanel pnlButton = createRowPanel();
        pnlButton.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlButton.add(btnSpin);
        pnlInputFields.add(pnlButton);

        pnlControls.add(pnlInputFields, BorderLayout.NORTH);
        pnlControls.add(Box.createVerticalGlue(), BorderLayout.CENTER);


        // 4. HISTORY AND RESULTS (South)
        JPanel pnlHistory = new JPanel(new BorderLayout(0, 5));
        pnlHistory.setOpaque(false);
        JLabel lblHistory = createLabel("History and Results:", labelFont);

        pnlHistory.add(lblHistory, BorderLayout.NORTH);
        pnlHistory.add(new JScrollPane(txtResults), BorderLayout.CENTER);

        pnlControls.add(pnlHistory, BorderLayout.SOUTH);

        return pnlControls;
    }

    // Auxiliary method for creating transparent rows
    private JPanel createRowPanel() {
        JPanel p = new JPanel();
        p.setOpaque(false); // Critical: Transparent
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, p.getPreferredSize().height));
        return p;
    }

    // Auxiliary method for creating aligned labels
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

                // NOTE: Additional logic needed for complex stakes (STREET, DOZEN, etc.)
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
            board.startSpin();

            Timer checkTimer = new Timer(500, null);
            checkTimer.addActionListener(e -> {
                if (board.isWheelStopped() && board.isBall1Stopped() && board.isBall2Stopped()) {
                    checkTimer.stop();
                    processSpinFinished(board.getResults());
                }
            });
            checkTimer.start();

        } catch (NumberFormatException | InvalidBetTypeException | NullStakeException ex) {
            showMessage("Input Error", "Input error: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            btnSpin.setEnabled(true);
        } catch (InsufficientBalanceException ex) {
            showMessage("Balance Error", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            btnSpin.setEnabled(true);
        }
    }

    /** Called when the animation stops to settle bets and display results. */
    private void processSpinFinished(Pocket[] results) {
        try {
            gameRound.settleBets(player, results);

            Bet lastBet = player.getCurrentBets().isEmpty() ? null : player.getCurrentBets().get(0);

            if (lastBet != null) {
                txtResults.append(generateResultMessage(lastBet, results, player.getWinningsThisRound()) + "\n");
            } else {
                txtResults.append("Spin: " + results[0].getNumber() + " | " + results[1].getNumber() + ".\n");
            }

            updateBalance();

        } catch (Exception ex) {
            showMessage("Calculation Error", "Error processing results: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        } finally {
            SwingUtilities.invokeLater(() -> btnSpin.setEnabled(true));
        }
    }

    // Auxiliary methods
    private void updateBalance() {
        lblBalance.setText("Balance: $" + String.format("%.2f", player.getBalance()));
    }
    private String generateResultMessage(Bet bet, Pocket[] results, double totalWinnings) {
        double netAmount = totalWinnings - bet.getAmount();
        String msg = "Spin: " + results[0].getNumber() + " and " + results[1].getNumber() + ". ";
        if (netAmount > 0) msg += "YOU WON! Net: $" + String.format("%.2f", netAmount) + ".";
        else msg += "Lost $" + String.format("%.2f", bet.getAmount()) + ".";
        return msg;
    }
    private void showMessage(String title, String message, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }
}