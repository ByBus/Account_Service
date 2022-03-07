package account.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = RoleChangeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleChangeCorrect {
    String message() default "DEFAULT_MESSAGE";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}