package exceptions;

public class BetOutOfRangeException extends RuntimeException {
    public BetOutOfRangeException() {
        super("Oops! You can only bet between $10 and $5000.");
    }
}
