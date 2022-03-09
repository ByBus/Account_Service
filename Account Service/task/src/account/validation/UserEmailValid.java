package account.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = UserEmailValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserEmailValid {
    String message() default "Date must be in MM-YYYY format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}