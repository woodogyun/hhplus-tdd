package io.hhplus.tdd.exception;

public class NegativeAmountException extends RuntimeException {

    public NegativeAmountException(String message) {
        super(message);
    }

    public NegativeAmountException(String message, Throwable cause) {
        super(message, cause);
    }
}
