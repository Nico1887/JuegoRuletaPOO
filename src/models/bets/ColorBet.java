package models.bets;

// ▶ Imports ────────────────────────────────────────────────────────────────────────────────────────────────────
import models.Player;
import models.Pocket;

public class ColorBet extends Bet {
    // ▶ Attributes ─────────────────────────────────────────────────────────────────────────────────────────────

    // ▶ Constructors ───────────────────────────────────────────────────────────────────────────────────────────
    public ColorBet(Player player, double amount) {
        super(player, amount);
    }

    // ▶ Getters ────────────────────────────────────────────────────────────────────────────────────────────────

    // ▶ Methods ────────────────────────────────────────────────────────────────────────────────────────────────
    @Override
    public double calculatePayout(Pocket[] results) {
        return 0;
    }

}
