package io.hhplus.tdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
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
    public void testValidate_NegativePoint() {
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
    public void testValidate_NegativeId() {
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
    public void testValidate_ValidUserPoint() {
        final long updateMillis = 1L;
        UserPoint userPoint = new UserPoint(1, 10, updateMillis);
        BindingResult bindingResult = new DataBinder(userPoint).getBindingResult();

        userPointValidator.validate(userPoint, bindingResult);

        assertTrue(!bindingResult.hasErrors());
    }
}
