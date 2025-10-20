package models.bets;

// ▶ Imports ────────────────────────────────────────────────────────────────────────────────────────────────────
import models.Player;
import models.Pocket;

public class StreetBet extends Bet {
    // ▶ Attributes ─────────────────────────────────────────────────────────────────────────────────────────────

    // ▶ Constructors ───────────────────────────────────────────────────────────────────────────────────────────
    public StreetBet(Player player, double amount) {
        super(player, amount);
    }

    // ▶ Getters ────────────────────────────────────────────────────────────────────────────────────────────────

    // ▶ Methods ────────────────────────────────────────────────────────────────────────────────────────────────
    @Override
    public double calculatePayout(Pocket[] results) {
        return 0;
    }

}
