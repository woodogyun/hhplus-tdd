package io.hhplus.tdd.vaildation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import io.hhplus.tdd.point.PointHistory;

@Deprecated
@Component
public class UserHistoryValidator implements Validator{
    
    @Override
    public boolean supports(Class<?> clazz) {
        return PointHistory.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PointHistory pointHistory = (PointHistory) target;

        if (pointHistory.id() < 0) {
            errors.rejectValue("id", "NegativeIdException", "Id must not be negative");
        }

        if (pointHistory.userId() < 0) {
            errors.rejectValue("userId", "NegativeIdException", "User Id must not be negative");
        }

        if (pointHistory.amount() < 0) {
            errors.rejectValue("amount", "NegativeAmountmountException", "Amount must not be negative");
        }
    }
}
