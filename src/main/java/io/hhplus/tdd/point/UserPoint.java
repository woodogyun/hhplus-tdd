package io.hhplus.tdd.point;

import io.hhplus.tdd.exception.NegativePointException;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {

    public UserPoint {
        if (point < 0) {
            throw new NegativePointException("Point cannot be negative: " + point);
        }
    }

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }
}
