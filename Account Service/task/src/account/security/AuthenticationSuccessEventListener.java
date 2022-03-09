package account.security;

import account.model.entity.UserEntity;
import account.repository.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthenticationSuccessEventListener implements
        ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RepositoryService repository;

    @Override
    public void onApplicationEvent(final AuthenticationSuccessEvent event) {
        String email = event.getAuthentication().getName();
        UserEntity user = repository.getUserByEmail(email);
        try {
            user.setFailedLoginAttempts(0);
            repository.update(user);
        } catch (Exception e) {
            System.out.println("_________________________NULL_USER________________________");
        }
    }
}
