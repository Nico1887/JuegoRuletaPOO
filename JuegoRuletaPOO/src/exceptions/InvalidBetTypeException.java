package exceptions;

public class InvalidBetTypeException extends RuntimeException {
    public InvalidBetTypeException() {
        super("Sorry, that type of bet isn’t allowed.");
    }
}
