package exceptions;

public class NullPlayerException extends RuntimeException {
    public NullPlayerException() {
        super("A bet must have a player assigned.");
    }
}
