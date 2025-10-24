package models.bets;

// ▶ ────────────────────────────────────────────────────────────────────────────────────────────────────────────
// Bet made by a player. Has type, chosen value, and wagered amount.
// ▶ ────────────────────────────────────────────────────────────────────────────────────────────────────────────

// ▶ Imports ────────────────────────────────────────────────────────────────────────────────────────────────────
import enums.generics.BetType;
import enums.stakes.*;
import exceptions.*;
import models.Player    ;
import models.Pocket    ;

public abstract class Bet<T> {

    // ▶ Attributes ─────────────────────────────────────────────────────────────────────────────────────────────

    // ▶ Static
    private static final double MIN_AMOUNT = 10.0   ;
    private static final double MAX_AMOUNT = 5000.0 ;

    // ▶ Instance attributes
    private final Player player ;    // Player who placed the bet.
    private final BetType type  ;    // Type of the bet (e.g., number, color, outcome, etc.).
    private final T stake       ;    // Specific item, number, or entity the player is betting on.
    private final double amount ;    // Amount of money wagered in the bet.

    // ▶ Constructors ───────────────────────────────────────────────────────────────────────────────────────────
    public Bet(Player player, BetType type, T stake, double amount) {
        // ▶ Exceptions (messages centralized in exception classes).
        if (player == null) {
            throw new NullPlayerException();
        }                              // Player must not be null.
        if (type == null) {
            throw new InvalidBetTypeException();
        }                                // Bet type must be valid.
        if (stake == null) {
            throw new NullStakeException();
        }                               // The player must choose something to bet on.
        if (!(isValidStake(stake, type))) {
            throw new InvalidStakeException();
        }                // Stake must be valid.
        if (amount > MAX_AMOUNT || amount < MIN_AMOUNT) {
            throw new BetOutOfRangeException();
        }  // Amount must be within allowed range.

        // ▶ Assignments
        this.player = player    ;
        this.type = type        ;
        this.stake = stake      ;
        this.amount = amount    ;
    }

    // ▶ Getters ────────────────────────────────────────────────────────────────────────────────────────────────
    public Player getPlayer() { return player; }
    public BetType getType() {return type; }
    public T getStake() {return stake; }
    public double getAmount() {return amount; }

    // ▶ Methods ────────────────────────────────────────────────────────────────────────────────────────────────
    public abstract double calculatePayout(Pocket[] results);

    // ▶ Validation  ────────────────────────────────────────────────────────────────────────────────────────────
    private boolean isValidStake(T stake, BetType type) {
        if (stake == null) {return false; }

        switch (type) {

            // ▶ The Classics
            case NUMBER :    return stake instanceof NumberStake    ;
            case PARITY :    return stake instanceof ParityStake    ;
            case STREET :    return stake instanceof StreetStake    ;
            case DOZEN  :    return stake instanceof DozenStake     ;

            // ▶ Rainbow Bets
            case COLOR          :   return stake instanceof ColorStake          ;
            case SINGLE_COLOR   :   return stake instanceof SingleColorStake    ;
            case DOUBLE_WIN     :   return stake instanceof DoubleWinStake      ;
            case COLOR_SEQUENCE :   return stake instanceof ColorSequenceStake  ;
            case LUCKY_BLACK    :   return stake instanceof LuckyBlackStake     ;

        }

        return false;
    }

}
