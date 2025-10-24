package enums.stakes;

// ▶ ────────────────────────────────────────────────────────────────────────────────────────────────────────────
// Wins if at least one of the balls is black.
// ▶ ────────────────────────────────────────────────────────────────────────────────────────────────────────────

public class LuckyBlackStake {

    // ▶ Methods ────────────────────────────────────────────────────────────────────────────────────────────────
    public boolean matches(ColorStake result1, ColorStake result2) {
        return result1 == ColorStake.BLACK || result2 == ColorStake.BLACK;
    }

}
