package account;

import account.auth.Role;
import account.model.entity.BreachedPasswordEntity;
import account.model.entity.RoleEntity;
import account.repository.BreachedPasswordRepository;
import account.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean isDBInitialized = false;
    private static final List<String> BREACHED_PASSWORDS = List.of(
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

    private static final List<Role> ROLES = Arrays.asList(Role.values());

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BreachedPasswordRepository passwordRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!isDBInitialized) {
            ROLES.forEach(this::createRole);
            BREACHED_PASSWORDS.forEach(this::createBreachedPassword);
            isDBInitialized = true;
        }
    }

    private void createBreachedPassword(String password) {
        BreachedPasswordEntity badPassword = passwordRepository.findById(password).orElse(null);
        if (badPassword == null) {
            badPassword = new BreachedPasswordEntity(password);
            passwordRepository.save(badPassword);
        }
    }

    private void createRole(Role roleType) {
        RoleEntity role = roleRepository.findByName(roleType.name());
        if (role == null) {
            role = new RoleEntity(roleType);
            role.setAdmin(roleType.equals(Role.ADMINISTRATOR));
            roleRepository.save(role);
        }
    }
}
