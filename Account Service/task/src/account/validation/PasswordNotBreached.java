package account.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = PasswordInBreachedListValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordNotBreached {
    String message() default "The password is in the hacker's database!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}