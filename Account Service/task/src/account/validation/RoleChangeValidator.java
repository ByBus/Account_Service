package account.validation;

import account.exception.RoleNotFoundException;
import account.exception.UserNotFoundException;
import account.model.dto.RoleChangeDTO;
import account.model.entity.RoleEntity;
import account.model.entity.UserEntity;
import account.repository.RepositoryService;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class RoleChangeValidator implements ConstraintValidator<RoleChangeCorrect, RoleChangeDTO> {
    @Autowired
    RepositoryService repository;

    @Override
    public boolean isValid(RoleChangeDTO roleChangeDTO, ConstraintValidatorContext context) {
        UserEntity user = repository.getUserByEmail(roleChangeDTO.getEmail());
        RoleEntity role;
        RoleOperation operation;
        if (user == null) {
            throw new UserNotFoundException();
        }
        try {
            role = repository.findRole(roleChangeDTO.getRole());
            role.getName(); //throws NullPointerException
            operation = RoleOperation.valueOf(roleChangeDTO.getOperation());
        } catch (NullPointerException e) {
            throw new RoleNotFoundException();
        } catch (IllegalArgumentException e) {
            setErrorMessage(context, "Operation must be REMOVE or GRANT!");
            return false;
        }
        switch (operation) {
            case REMOVE:
                if (role.isAdmin()) {
                    setErrorMessage(context, "Can't remove ADMINISTRATOR role!");
                    return false;
                }
                if (!user.getRoles().contains(role)) {
                    setErrorMessage(context, "The user does not have a role!");
                    return false;
                }
                if (user.getRoles().size() == 1 && user.getRoles().contains(role)) {
                    setErrorMessage(context, "The user must have at least one role!");
                    return false;
                }
                break;
            case GRANT:
                boolean isUserAdmin = Objects.requireNonNull(user.getRoles().stream().findAny().get()).isAdmin();
                if (isUserAdmin != role.isAdmin()) {
                    setErrorMessage(context, "The user cannot combine administrative and business roles!");
                    return false;
                }
                break;
        }
        return true;
    }

    private void setErrorMessage(ConstraintValidatorContext context, String message) {
        HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
        hibernateContext.disableDefaultConstraintViolation();
        hibernateContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
