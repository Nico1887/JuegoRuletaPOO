package models.bets;

// Bet made by a player
// Has type, chosen value, and wagered amount

// ▶ Imports ────────────────────────────────────────────────────────────────────────────────────────────────────
import models.Player;
import models.Pocket;

public abstract class Bet {
    // ▶ Attributes ─────────────────────────────────────────────────────────────────────────────────────────────
    private final Player player;
    private final double amount;

    // ▶ Constructors ───────────────────────────────────────────────────────────────────────────────────────────
    public Bet(Player player, double amount) {
        this.amount = amount;
        this.player = player;
    }

    // ▶ Getters ────────────────────────────────────────────────────────────────────────────────────────────────
    public double getAmount() {
        return amount;
    }
    public Player getPlayer() { return player; }

    // ▶ Methods ────────────────────────────────────────────────────────────────────────────────────────────────
    public abstract double calculatePayout(Pocket[] results);

}
