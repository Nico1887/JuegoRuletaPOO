package enums.stakes;

// ▶ ────────────────────────────────────────────────────────────────────────────────────────────────────────────
// Stake for a Parity bet; wins only if both balls have the chosen parity.
// ▶ ────────────────────────────────────────────────────────────────────────────────────────────────────────────

public enum ParityStake {

    // ▶ Cases ──────────────────────────────────────────────────────────────────────────────────────────────────

    EVEN    ,   // Numbers like: {2,4,6}
    ODD     ;   // Numbers like: {1,3,5}

    // ▶ Methods ────────────────────────────────────────────────────────────────────────────────────────────────
    public boolean matches(int result1, int result2) {
        boolean even1 = result1 %2 == 0;
        boolean even2 = result2 %2 == 0;

        // If Player chose to bet on EVEN:
        if (this == EVEN) {
            return even1 && even2;
        }

        // If Player chose to bet on ODD:
        else {
            return !even1 && !even2;
        }
    }

}