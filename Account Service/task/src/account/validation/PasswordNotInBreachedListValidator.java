package account.validation;

import account.repository.BreachedPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class PasswordNotInBreachedListValidator implements ConstraintValidator<PasswordNotBreached, String> {
    @Autowired
    BreachedPasswordRepository repository;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null && repository.findById(password).isEmpty();
    }
}
