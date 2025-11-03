package logic;

import enums.generics.BetType;
import enums.stakes.ColorStake;
import enums.stakes.NumberStake;
import enums.stakes.ParityStake;
import enums.stakes.LuckyBlackStake;
import enums.stakes.DoubleWinStake;
import enums.stakes.StreetStake;
import enums.stakes.DozenStake;
import enums.stakes.ColorSequenceStake;

import models.Pocket;
import models.bets.Bet;
import java.util.List;

/**
 * Calculates the payout multiplier for any given Bet based on the spin results.
 * Uses the 'matches()' methods provided by the Stake classes.
 */
public class PayoutCalculator {

    // ▶ Constructors ───────────────────────────────────────────────────────────────────────────────────────────
    public PayoutCalculator() {
        // Default constructor
    }

    /**
     * Calculates the payout multiplier based on the Bet object and winning pockets.
     * Multipliers are calculated as (1 + Net Gain Multiplier).
     * @param bet The bet placed by the player.
     * @param results The two winning pockets (r1 and r2).
     * @return The total multiplier (e.g., 2.0 for 1:1 payout).
     */
    public double calculateMultiplier(Bet bet, Pocket[] results) {
        double multiplier = 0.0;
        final Pocket r1 = results[0];
        final Pocket r2 = results[1];

        // The stake value (T)
        Object stake = bet.getStake();

        // Flags for special rules
        boolean isLuckyBlackWin = r1.getColor() == ColorStake.BLACK || r2.getColor() == ColorStake.BLACK;
        boolean betWasOnBlack = false;

        switch (bet.getType()) {

            // Payout 35:1 -> Multiplier 36.0
            case NUMBER:
                if (stake instanceof NumberStake) {
                    NumberStake ns = (NumberStake) stake;
                    if (ns.matches(r1.getNumber(), r2.getNumber())) {
                        multiplier = 36.0;
                    }
                }
                break;

            // Payout 1:1 -> Multiplier 2.0
            case PARITY:
                if (stake instanceof ParityStake) {
                    ParityStake ps = (ParityStake) stake;
                    if (ps.matches(r1.getNumber(), r2.getNumber())) {
                        multiplier = 2.0;
                    }
                }
                break;

            // Payout 2:1 (DOZEN) - Requires DozenStake
            case DOZEN:
                if (stake instanceof DozenStake) {
                    // NOTE: Assuming DozenStake has a matches method based on number
                    // if (((DozenStake) stake).matches(r1.getNumber(), r2.getNumber())) {
                    //     multiplier = 3.0;
                    // }
                }
                break;

            // Payout 1:1 (COLOR - At least one ball matches)
            case COLOR:
                if (stake instanceof ColorStake) {
                    ColorStake cs = (ColorStake) stake;
                    if (cs == ColorStake.BLACK) betWasOnBlack = true;

                    // Uses matches() method from ColorStake enum (which checks OR condition)
                    if (cs.matches(r1.getColor(), r2.getColor())) {
                        multiplier = 2.0;
                    }
                }
                break;

            // Payout 5:1 (SINGLE COLOR - Both balls match)
            case SINGLE_COLOR:
                if (stake instanceof ColorStake) {
                    ColorStake cs = (ColorStake) stake;
                    if (cs == ColorStake.BLACK) betWasOnBlack = true;

                    // Requires both results to match the stake color
                    if (r1.getColor() == cs && r2.getColor() == cs) {
                        multiplier = 6.0;
                    }
                }
                break;

            // Payout 8:1 (DOUBLE WIN - Any order)
            case DOUBLE_WIN:
                if (stake instanceof DoubleWinStake) {
                    DoubleWinStake dws = (DoubleWinStake) stake;
                    // NOTE: Assumes DoubleWinStake has a matches method based on colors
                    if (dws.matches(r1.getColor(), r2.getColor())) {
                        multiplier = 9.0;
                    }
                }
                break;

            // Payout 12:1 (STREET)
            case STREET:
                if (stake instanceof StreetStake) {
                    // NOTE: Assumes StreetStake has a matches method based on number
                    // if (((StreetStake) stake).matches(r1.getNumber(), r2.getNumber())) {
                    //     multiplier = 13.0;
                    // }
                }
                break;

            // Payout 15:1 (COLOR SEQUENCE - Exact order)
            case COLOR_SEQUENCE:
                if (stake instanceof ColorSequenceStake) {
                    // NOTE: Assumes ColorSequenceStake has a matches method based on color order
                    // if (((ColorSequenceStake) stake).matches(r1.getColor(), r2.getColor())) {
                    //     multiplier = 16.0;
                    // }
                }
                break;

            // Payout 0:1 (LUCKY BLACK - Stake returned)
            case LUCKY_BLACK:
                if (stake instanceof LuckyBlackStake) {
                    LuckyBlackStake lbs = (LuckyBlackStake) stake;
                    // Uses matches() method from LuckyBlackStake (checks if AT LEAST one is BLACK)
                    if (lbs.matches(r1.getColor(), r2.getColor())) {
                        multiplier = 1.0;
                    }
                }
                break;
        }

        // --- LUCKY BLACK MODIFIER RULE ---
        if (multiplier == 0.0) return 0.0;

        // Doubles the net gain if a black pocket is hit, UNLESS the bet was placed on BLACK.
        if (isLuckyBlackWin && !betWasOnBlack) {
            double originalNetGain = multiplier - 1.0;
            multiplier = 1.0 + (2.0 * originalNetGain);
        }

        return multiplier;
    }
}