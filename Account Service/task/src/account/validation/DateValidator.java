package account.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<DateValidation, String> {
    @Override
    public boolean isValid(String string, ConstraintValidatorContext context) {
        return string == null || string.matches("(0[1-9]|1[0-2])-\\d{4}");
    }
}
