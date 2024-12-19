package io.hhplus.tdd.vaildation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import io.hhplus.tdd.point.UserPoint;

@Deprecated
@Component
public class UserPointValidator implements Validator{
    
    @Override
    public boolean supports(Class<?> clazz) {
        return UserPoint.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserPoint userPoint = (UserPoint) target;

        if (userPoint.point() < 0) {
            errors.rejectValue("point", "NegativePointException", "Point must not be negative");
        }

        if (userPoint.id() < 0) {
            errors.rejectValue("id", "NegativeIdException", "ID must not be negative");
        }
    }
}
