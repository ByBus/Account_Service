package account.validation;

import account.auth.Role;
import account.exception.AdminDeletionException;
import account.exception.UserNotFoundException;
import account.model.entity.RoleEntity;
import account.model.entity.UserEntity;
import account.repository.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserEmailValidator implements ConstraintValidator<UserEmailValid, String> {
    @Autowired
    RepositoryService repository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        UserEntity user = repository.getUserByEmail(email);
        RoleEntity admin = repository.findRole(Role.ADMINISTRATOR);
        if (user == null) {
            throw new UserNotFoundException();
        }
        if (user.getRoles().contains(admin)) {
            throw new AdminDeletionException();
        }
        return true;
    }
}
