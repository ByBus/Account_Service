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
        if (user == null) {
            throw new UserNotFoundException();
        }
        String roleName = roleChangeDTO.getRole();
        RoleEntity newRole = repository.findRole(roleName);
        if (newRole == null) {
            throw new RoleNotFoundException();
        }
        switch (roleChangeDTO.getOperation()) {
            case "REMOVE":
                if (roleName.equals("ADMINISTRATOR")) {
                    setErrorMessage(context, "Can't remove ADMINISTRATOR role!");
                    return false;
                }
                if (!user.getRoles().contains(newRole)) {
                    setErrorMessage(context, "The user does not have a role!");
                    return false;
                }
                if (user.getRoles().size() == 1 && user.getRoles().contains(newRole)) {
                    setErrorMessage(context, "The user must have at least one role!");
                    return false;
                }
                break;
            case "GRANT":
                boolean isUserAdmin = Objects.requireNonNull(user.getRoles().stream().findAny().get()).isAdmin();
                if (isUserAdmin != newRole.isAdmin()) {
                    setErrorMessage(context, "The user cannot combine administrative and business roles!");
                    return false;
                }
                break;
            default:
                setErrorMessage(context, "Incorrect OPERATION! (REMOVE, GRANT)");
                return false;
        }
        return true;
    }

    private void setErrorMessage(ConstraintValidatorContext context, String message) {
        HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
        hibernateContext.disableDefaultConstraintViolation();
        hibernateContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
