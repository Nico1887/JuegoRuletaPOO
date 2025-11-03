package exceptions;

public class InvalidStakeException extends RuntimeException {
    public InvalidStakeException() {
        super("Your choice doesn’t match the bet type you selected.");
    }
}