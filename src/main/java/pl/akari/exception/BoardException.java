package main.java.pl.akari.exception;

public class BoardException extends RuntimeException {
    public BoardException(String message) {
        super(message);
    }

    public BoardException(String message, Throwable cause) {
        super(message, cause);
    }
}
