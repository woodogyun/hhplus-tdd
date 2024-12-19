package io.hhplus.tdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.vaildation.UserHistoryValidator;

public class UserHistoryVaildtorTest {
    private UserHistoryValidator userHistoryValidator;

    @BeforeEach
    public void setUp() {
        userHistoryValidator = new UserHistoryValidator();
    }
    
    @Test
    @DisplayName("아이디 음수 체크")
    public void 아이디_음수_체크() {
        final long updateMillis = 1L;
        PointHistory pointHistory = new PointHistory(-1, 1, 10, TransactionType.CHARGE ,updateMillis);
        BindingResult bindingResult = new DataBinder(pointHistory).getBindingResult();

        userHistoryValidator.validate(pointHistory, bindingResult);

        assertTrue(bindingResult.hasErrors());
        assertEquals(1, bindingResult.getErrorCount());
        ObjectError error = bindingResult.getAllErrors().get(0);
        assertEquals("NegativeIdException", error.getCode());
        assertEquals("Id must not be negative", error.getDefaultMessage());
    }

    @Test
    @DisplayName("유저 아이디 음수 체크")
    public void 유저_아이디_음수_체크() {
        final long updateMillis = 1L;
        PointHistory pointHistory = new PointHistory(1, -1, 10, TransactionType.CHARGE ,updateMillis);
        BindingResult bindingResult = new DataBinder(pointHistory).getBindingResult();

        userHistoryValidator.validate(pointHistory, bindingResult);

        assertTrue(bindingResult.hasErrors());
        assertEquals(1, bindingResult.getErrorCount());
        ObjectError error = bindingResult.getAllErrors().get(0);
        assertEquals("NegativeIdException", error.getCode());
        assertEquals("User Id must not be negative", error.getDefaultMessage());
    }

    @Test
    @DisplayName("포인트 음수 체크")
    public void 포인트_음수_체크() {
        final long updateMillis = 1L;
        PointHistory pointHistory = new PointHistory(1, 1, -10, TransactionType.CHARGE ,updateMillis);
        BindingResult bindingResult = new DataBinder(pointHistory).getBindingResult();

        userHistoryValidator.validate(pointHistory, bindingResult);

        assertTrue(bindingResult.hasErrors());
        assertEquals(1, bindingResult.getErrorCount());
        ObjectError error = bindingResult.getAllErrors().get(0);
        assertEquals("NegativeAmountmountException", error.getCode());
        assertEquals("Amount must not be negative", error.getDefaultMessage());
    }

    @Test
    @DisplayName("유저 이력 체크")
    public void 유저_이력_체크() {
        final long updateMillis = 1L;
        PointHistory pointHistory = new PointHistory(1, 1, 10, TransactionType.CHARGE ,updateMillis);
        BindingResult bindingResult = new DataBinder(pointHistory).getBindingResult();

        userHistoryValidator.validate(pointHistory, bindingResult);
        assertTrue(!bindingResult.hasErrors());
    }

}
