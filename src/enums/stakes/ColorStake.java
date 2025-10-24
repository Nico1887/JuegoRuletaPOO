package enums.stakes;

// ▶ ────────────────────────────────────────────────────────────────────────────────────────────────────────────
// Stakes for a Simple Color bet; wins if at least one ball lands in the chosen color.
// ▶ ────────────────────────────────────────────────────────────────────────────────────────────────────────────

public enum ColorStake {

    // ▶ Cases ──────────────────────────────────────────────────────────────────────────────────────────────────

    // ▶ Standard Colors (48 pockets)   ─────────────────────────────────────────────────────────────────────────
    RED     ,     // 8 pockets
    ORANGE  ,     // 8 pockets
    YELLOW  ,     // 8 pockets
    GREEN   ,     // 8 pockets
    BLUE    ,     // 8 pockets
    VIOLET  ,     // 8 pockets

    // ▶ Special Colors (2 pockets)   ───────────────────────────────────────────────────────────────────────────
    BLACK   ;     // positions 25 and 50

    // ▶ Methods ────────────────────────────────────────────────────────────────────────────────────────────────
    public boolean matches(ColorStake result1, ColorStake result2) {
        return result1 == this || result2 == this; // 'this' refers to the current instance.
    }

}