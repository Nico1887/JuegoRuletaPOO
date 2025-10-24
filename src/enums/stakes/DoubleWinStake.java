package enums.stakes;

// ▶ ────────────────────────────────────────────────────────────────────────────────────────────────────────────
// PENDING.
// ▶ ────────────────────────────────────────────────────────────────────────────────────────────────────────────

public class DoubleWinStake {

    // ▶ Attributes ─────────────────────────────────────────────────────────────────────────────────────────────
    private final ColorStake chosenColor1;
    private final ColorStake chosenColor2;

    // ▶ Constructor ────────────────────────────────────────────────────────────────────────────────────────────
    DoubleWinStake(ColorStake color1, ColorStake color2) {
        this.chosenColor1 = color1;
        this.chosenColor2 = color2;
    }

    // ▶ Methods ────────────────────────────────────────────────────────────────────────────────────────────────
    public boolean matches(ColorStake result1, ColorStake result2) {
        if (result1 == result2) {
            return false;
        }

        return (result1 == chosenColor1 && result2 == chosenColor2)
                || (result1 ==chosenColor2 && result2 == chosenColor1);

    }

}
