package io.hhplus.tdd.exception;

public class NegativeIdException extends RuntimeException {

    public NegativeIdException(String message) {
        super(message);
    }

    public NegativeIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
