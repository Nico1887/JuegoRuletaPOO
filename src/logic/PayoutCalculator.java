package logic;

import enums.generics.BetType;
import enums.stakes.*;
import models.Pocket;
import models.bets.Bet;

public class PayoutCalculator {

    public PayoutCalculator() {
    }

    /**
     * @param bet     specific bet (type + stake + amount)
     * @param results winning pockets [r1, r2]
     * @return total multiplier (2.0 == 1:1; 1.0 == stake refund)
     */
    public double calculateMultiplier(Bet bet, Pocket[] results) {
        double multiplier = 0.0;

        final Pocket r1 = results[0];
        final Pocket r2 = results[1];

        Object stake = bet.getStake();

        boolean isLuckyBlackWin =
                r1.getColor() == ColorStake.BLACK || r2.getColor() == ColorStake.BLACK;

        boolean betWasOnBlack = false;

        switch (bet.getType()) {

            // 35:1 -> 36.0
            case NUMBER:
                if (stake instanceof NumberStake) {
                    NumberStake ns = (NumberStake) stake;
                    if (ns.matches(r1.getNumber(), r2.getNumber())) {
                        multiplier = 36.0;
                    }
                }
                break;

            // 1:1 -> 2.0 (both must match in parity)
            case PARITY:
                if (stake instanceof ParityStake) {
                    ParityStake ps = (ParityStake) stake;
                    if (ps.matches(r1.getNumber(), r2.getNumber())) {
                        multiplier = 2.0;
                    }
                }
                break;

            // 2:1 -> 3.0 (at least one ball in the “dozen/block”)
            case DOZEN:
                if (stake instanceof DozenStake) {
                    DozenStake ds = (DozenStake) stake;
                    if (ds.matches(r1.getNumber(), r2.getNumber())) {
                        multiplier = 3.0;
                    }
                }
                break;

            // 1:1 -> 2.0 (at least one ball of the chosen color)
            case COLOR:
                if (stake instanceof ColorStake) {
                    ColorStake cs = (ColorStake) stake;
                    if (cs == ColorStake.BLACK) betWasOnBlack = true;

                    if (cs.matches(r1.getColor(), r2.getColor())) {
                        multiplier = 2.0;
                    }
                }
                break;

            // 5:1 -> 6.0 (both balls of the same color)
            case SINGLE_COLOR:
                if (stake instanceof ColorStake) {
                    ColorStake cs = (ColorStake) stake;
                    if (cs == ColorStake.BLACK) betWasOnBlack = true;

                    if (r1.getColor() == cs && r2.getColor() == cs) {
                        multiplier = 6.0;
                    }
                }
                break;

            // 8:1 -> 9.0 (two different colors, in any order)
            case DOUBLE_WIN:
                if (stake instanceof DoubleWinStake) {
                    DoubleWinStake dws = (DoubleWinStake) stake;
                    if (dws.matches(r1.getColor(), r2.getColor())) {
                        multiplier = 9.0;
                    }
                }
                break;

            // 12:1 -> 13.0 (at least one ball inside the street)
            case STREET:
                if (stake instanceof StreetStake) {
                    StreetStake ss = (StreetStake) stake;
                    if (ss.matches(r1.getNumber(), r2.getNumber())) {
                        multiplier = 13.0;
                    }
                }
                break;

            // 15:1 -> 16.0 (exact color sequence)
            case COLOR_SEQUENCE:
                if (stake instanceof ColorSequenceStake) {
                    ColorSequenceStake css = (ColorSequenceStake) stake;
                    if (css.matches(r1.getColor(), r2.getColor())) {
                        multiplier = 16.0;
                    }
                }
                break;

            // 0:1 -> 1.0 (refund if at least one black appears)
            case LUCKY_BLACK:
                if (stake instanceof LuckyBlackStake) {
                    LuckyBlackStake lbs = (LuckyBlackStake) stake;
                    if (lbs.matches(r1.getColor(), r2.getColor())) {
                        multiplier = 1.0;
                    }
                }
                break;
        }

        // If no win, no multiplier
        if (multiplier == 0.0) return 0.0;
        return 0.0;
    }
}

        // Lucky Black as a NET WIN MODIFIER
