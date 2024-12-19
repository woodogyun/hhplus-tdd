package io.hhplus.tdd.exception;

public class NegativePointException extends RuntimeException {

    public NegativePointException(String message) {
        super(message);
    }

    public NegativePointException(String message, Throwable cause) {
        super(message, cause);
    }
}
