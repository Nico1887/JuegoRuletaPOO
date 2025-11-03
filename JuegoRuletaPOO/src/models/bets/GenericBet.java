package models.bets;

import enums.generics.BetType;
import models.Player;
import models.Pocket;

/**
 * Concrete implementation of the abstract Bet class, used to instantiate any bet type
 * without creating individual classes for each stake (NumberBet, ColorBet, etc.).
 */
public class GenericBet<T> extends Bet<T> {

    public GenericBet(Player player, BetType type, T stake, double amount) {
        super(player, type, stake, amount);
    }

    /**
     * Implements the abstract method, simply returning 0.0 as the actual multiplier
     * calculation is delegated to PayoutCalculator.
     */
    @Override
    public double calculatePayout(Pocket[] results) {
        return 0.0;
    }
}