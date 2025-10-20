package models.bets;

// ▶ Imports ────────────────────────────────────────────────────────────────────────────────────────────────────
import models.Player;
import models.Pocket;

public class LuckyBlackBet extends Bet {
    // ▶ Attributes ─────────────────────────────────────────────────────────────────────────────────────────────

    // ▶ Constructors ───────────────────────────────────────────────────────────────────────────────────────────
    public LuckyBlackBet(Player player, double amount) {
        super(player, amount);
    }

    // ▶ Getters ────────────────────────────────────────────────────────────────────────────────────────────────

    // ▶ Methods ────────────────────────────────────────────────────────────────────────────────────────────────
    @Override
    public double calculatePayout(Pocket[] results) {
        return 0;
    }

}
