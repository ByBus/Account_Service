package account.security;

import account.auth.Role;
import account.auth.UserInfo;
import account.buiseness.EventType;
import account.buiseness.Logger;
import account.model.entity.RoleEntity;
import account.model.entity.UserEntity;
import account.repository.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final RepositoryService repository;
    private final Logger logger;

    @Autowired
    public AuthenticationFailureListener(RepositoryService repository,
                                         Logger logger) {
        this.repository = repository;
        this.logger = logger;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String email = event.getAuthentication().getName();
        UserEntity user = repository.getUserByEmail(email);
        RoleEntity admin = repository.findRole(Role.ADMINISTRATOR);

        logger.log(EventType.LOGIN_FAILED, email);
        try {
            int attempts = user.getFailedLoginAttempts() + 1;
            if (!user.getRoles().contains(admin)) {
                user.setFailedLoginAttempts(attempts);
                repository.update(user);
            }
            UserDetails userWrapped = new UserInfo(user);
            if (!userWrapped.isAccountNonLocked()) {
                logger.log(EventType.BRUTE_FORCE, email);
                logger.log(EventType.LOCK_USER, email, "Lock user " + email);
            }
        } catch (Exception e) {
            System.out.println("_________________________NULL_USER________________________");
        }
    }
}
