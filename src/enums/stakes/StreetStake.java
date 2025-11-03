package enums.stakes;

/*“Calle” = 3 números consecutivos.
Elegimos calles [1–3], [4–6], …, [46–48].
(49 y 50 se quedan fuera de “street” para mantener el tamaño de 3).*/
public enum StreetStake {
    S1_3(1,3),   S4_6(4,6),   S7_9(7,9),   S10_12(10,12),
    S13_15(13,15), S16_18(16,18), S19_21(19,21), S22_24(22,24),
    S25_27(25,27), S28_30(28,30), S31_33(31,33), S34_36(34,36),
    S37_39(37,39), S40_42(40,42), S43_45(43,45), S46_48(46,48);

    private final int from, to;

    StreetStake(int from, int to) {
        this.from = from;
        this.to = to;
    }

    /*Gana si al menos una de las bolas cae en la calle. */
    public boolean matches(int r1, int r2) {
        return inRange(r1) || inRange(r2);
    }

    private boolean inRange(int n) {
        return n >= from && n <= to;
    }
}