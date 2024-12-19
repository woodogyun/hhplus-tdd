package io.hhplus.tdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.vaildation.UserPointValidator;

public class UserPointVaildtorTest {
    private UserPointValidator userPointValidator;

    @BeforeEach
    public void setUp() {
        userPointValidator = new UserPointValidator();
    }

    @Test
    @DisplayName("유저 포인트 음수 체크")
    public void 유저_포인트_음수_체크() {
        final long updateMillis = 1L;
        UserPoint userPoint = new UserPoint(1, -10, updateMillis);
        BindingResult bindingResult = new DataBinder(userPoint).getBindingResult();

        userPointValidator.validate(userPoint, bindingResult);

        assertTrue(bindingResult.hasErrors());
        assertEquals(1, bindingResult.getErrorCount());
        ObjectError error = bindingResult.getAllErrors().get(0);
        assertEquals("NegativePointException", error.getCode());
        assertEquals("Point must not be negative", error.getDefaultMessage());
    }

    @Test
    @DisplayName("유저 포인트 음수 체크")
    public void 유저_아이디_음수_체크() {
        final long updateMillis = 1L;
        UserPoint userPoint = new UserPoint(-1, 10, updateMillis);
        BindingResult bindingResult = new DataBinder(userPoint).getBindingResult();

        userPointValidator.validate(userPoint, bindingResult);

        assertTrue(bindingResult.hasErrors());
        assertEquals(1, bindingResult.getErrorCount());
        ObjectError error = bindingResult.getAllErrors().get(0);
        assertEquals("NegativeIdException", error.getCode());
        assertEquals("ID must not be negative", error.getDefaultMessage());
    }

    @Test
    @DisplayName("유저 검증 체크")
    public void 유저_검증_체크() {
        final long updateMillis = 1L;
        UserPoint userPoint = new UserPoint(1, 10, updateMillis);
        BindingResult bindingResult = new DataBinder(userPoint).getBindingResult();

        userPointValidator.validate(userPoint, bindingResult);

        assertTrue(!bindingResult.hasErrors());
    }
}
