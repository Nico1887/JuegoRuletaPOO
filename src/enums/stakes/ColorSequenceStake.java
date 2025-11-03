package enums.stakes;

public class ColorSequenceStake {
    private final ColorStake first;
    private final ColorStake second;

    public ColorSequenceStake(ColorStake first, ColorStake second) {
        this.first = first;
        this.second = second;
    }

    /** Orden estricto: r1 debe ser first y r2 second. */
    public boolean matches(ColorStake r1, ColorStake r2) {
        return r1 == first && r2 == second;
    }

    public ColorStake getFirst()  { return first; }
    public ColorStake getSecond() { return second; }
}

