package exceptions;

public class InvalidBetTypeException extends RuntimeException {
    public InvalidBetTypeException(String message) {
        super(message);
    }
}
