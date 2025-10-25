package logic;

import models.Player;
import models.bets.Bet;
import models.Pocket;
import models.Table;

import java.util.List;

/**
 * Manages the core game logic: coordinating the spin, calculating payouts, and settling player balances.
 */
public class GameRound {
    // ▶ Attributes ─────────────────────────────────────────────────────────────────────────────────────────────
    private final PayoutCalculator payoutCalculator;
    private final Table table;

    // ▶ Constructors ───────────────────────────────────────────────────────────────────────────────────────────
    public GameRound(Table table, PayoutCalculator payoutCalculator) {
        this.table = table;
        this.payoutCalculator = payoutCalculator;
    }

    // ▶ Methods ────────────────────────────────────────────────────────────────────────────────────────────────

    /**
     * Processes all current bets placed by a player and liquidates the winnings.
     * @param player The player whose bets are being settled.
     * @param results The two winning Pocket results of the spin.
     */
    public void settleBets(Player player, Pocket[] results) {
        double totalWinnings = 0.0;
        List<Bet> bets = player.getCurrentBets();

        for (Bet bet : bets) {
            double multiplier = payoutCalculator.calculateMultiplier(bet, results);
            double winningAmount = bet.getAmount() * multiplier;

            // Only non-zero amounts contribute to the total winnings pool
            if (winningAmount > 0) {
                totalWinnings += winningAmount;
            }
        }

        // Add all winnings to the player's balance and clear the round state
        player.addWinnings(totalWinnings);
        player.addWinnings();
        player.clearBets();
    }

    /**
     * Simulates the wheel spin and returns the two random winning pockets.
     * @return An array containing the two winning Pocket objects.
     */
    public Pocket[] spin() {
        List<Pocket> pockets = table.getPockets();
        int numPockets = pockets.size();

        Pocket winner1 = pockets.get((int) (Math.random() * numPockets));
        Pocket winner2 = pockets.get((int) (Math.random() * numPockets));

        return new Pocket[]{winner1, winner2};
    }
}