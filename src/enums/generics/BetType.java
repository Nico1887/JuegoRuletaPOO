package enums.generics;

public enum BetType {

    // THE CLASSICS
    NUMBER          ,   // Bet on an exact number.
    PARITY          ,   // Bet on parity: EVEN or ODD. (only one bet)
    STREET          ,   // Bet on a street (three consecutive numbers).
    DOZEN           ,   // Bet on a dozen (range of 12 numbers).

    // RAINBOW BETS
    COLOR           ,   // Wins if at least one ball lands in the chosen color.
    SINGLE_COLOR    ,   // Wins if both balls land in the same chosen color.
    DOUBLE_WIN      ,   // Wins if both balls land in the two specified colors, in any order.
    COLOR_SEQUENCE  ,   // Wins if both balls land in the specified colors in the exact order.
    LUCKY_BLACK     ,   // TODO Add explanation

}