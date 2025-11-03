package enums.stakes;

// ▶ ────────────────────────────────────────────────────────────────────────────────────────────────────────────
// Stake for a Number bet; wins if at least one ball shows the chosen number.
// ▶ ────────────────────────────────────────────────────────────────────────────────────────────────────────────

public class NumberStake {
    // ▶ Attributes ─────────────────────────────────────────────────────────────────────────────────────────────
    private final int number;

    // ▶ Constructors ───────────────────────────────────────────────────────────────────────────────────────────
    public NumberStake(int number) {
        // ▶ Exceptions
        if (number < 1 || number > 50) {
            throw new IllegalArgumentException("Number must be between 1 and 50.");
        }

        // ▶ Assignments
        this.number = number;
    }

    // ▶ Getters ────────────────────────────────────────────────────────────────────────────────────────────────
    public int getNumber() {
        return number;
    }

    // ▶ Methods ────────────────────────────────────────────────────────────────────────────────────────────────
    public boolean matches(int result1, int result2) {
        return result1 == number || result2 == number;
    }

}