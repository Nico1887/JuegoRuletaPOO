package exceptions;

public class NullStakeException extends RuntimeException {
    public NullStakeException() {
        super("Please choose something to bet on.");
    }
}
