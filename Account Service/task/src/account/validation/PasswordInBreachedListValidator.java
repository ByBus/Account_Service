package account.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class PasswordInBreachedListValidator implements ConstraintValidator<PasswordNotBreached, String> {
    private final List<String> breachedPasswords = List.of(
            "PasswordForJanuary",
            "PasswordForFebruary",
            "PasswordForMarch",
            "PasswordForApril",
            "PasswordForMay",
            "PasswordForJune",
            "PasswordForJuly",
            "PasswordForAugust",
            "PasswordForSeptember",
            "PasswordForOctober",
            "PasswordForNovember",
            "PasswordForDecember");

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null && !breachedPasswords.contains(password);
    }
}
