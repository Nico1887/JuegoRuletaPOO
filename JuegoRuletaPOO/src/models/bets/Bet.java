package models.bets;

import enums.generics.BetType;
import enums.stakes.*;
import exceptions.*;
import models.Player    ;
import models.Pocket    ;

public abstract class Bet<T> {

    // ▶ Attributes ─────────────────────────────────────────────────────────────────────────────────────────────
    private static final double MIN_AMOUNT = 10.0   ;
    private static final double MAX_AMOUNT = 5000.0 ;

    private final Player player ;
    private final BetType type  ;
    private final T stake       ;
    private final double amount ;

    // ▶ Constructors ───────────────────────────────────────────────────────────────────────────────────────────
    public Bet(Player player, BetType type, T stake, double amount) {
        if (player == null) {
            throw new NullPlayerException();
        }
        if (type == null) {
            throw new InvalidBetTypeException();
        }
        if (stake == null) {
            throw new NullStakeException();
        }
        // NOTE: The isValidStake method is assumed to be correct based on the last version sent.
        if (!(isValidStake(stake, type))) {
            throw new InvalidStakeException();
        }
        if (amount > MAX_AMOUNT || amount < MIN_AMOUNT) {
            throw new BetOutOfRangeException();
        }

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
            case NUMBER :    return stake instanceof NumberStake    ;
            case PARITY :    return stake instanceof ParityStake    ;
            case STREET :    return stake instanceof StreetStake    ;
            case DOZEN  :    return stake instanceof DozenStake     ;

            case COLOR          :   return stake instanceof ColorStake          ;
            case SINGLE_COLOR   :   return stake instanceof ColorStake          ;
            case DOUBLE_WIN     :   return stake instanceof DoubleWinStake      ;
            case COLOR_SEQUENCE :   return stake instanceof ColorSequenceStake  ;
            case LUCKY_BLACK    :   return stake instanceof LuckyBlackStake     ;
        }
        return false;
    }
}