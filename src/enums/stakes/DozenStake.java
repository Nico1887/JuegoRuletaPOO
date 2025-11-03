package enums.stakes;

/*“Docenas” adaptadas al tablero 1..50:
Usamos 5 bloques de 10 (D1..D5) para cubrir todo el rango con payout 2:1.
(Es una regla de la versión Rainbow, no la ruleta clásica).*/
public enum DozenStake {
    D1(1,10), D2(11,20), D3(21,30), D4(31,40), D5(41,50);

    private final int from, to;

    DozenStake(int from, int to) {
        this.from = from;
        this.to = to;
    }

    /*Gana si al menos una bola cae dentro del bloque. */
    public boolean matches(int r1, int r2) {
        return inRange(r1) || inRange(r2);
    }

    private boolean inRange(int n) {
        return n >= from && n <= to;
    }
}
