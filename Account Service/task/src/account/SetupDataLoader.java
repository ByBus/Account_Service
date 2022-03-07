package account;

import account.model.entity.BreachedPasswordEntity;
import account.model.entity.RoleEntity;
import account.repository.BreachedPasswordRepository;
import account.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean isDBInitialized = false;
    private final List<String> breachedPasswords = List.of(
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

    private final List<String> roles = List.of("ADMINISTRATOR", "ACCOUNTANT", "USER");

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BreachedPasswordRepository passwordRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!isDBInitialized) {
            roles.forEach(this::createRole);
            breachedPasswords.forEach(this::createBreachedPassword);
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

    private void createRole(String name) {
        RoleEntity role = roleRepository.findByName(name);
        if (role == null) {
            role = new RoleEntity(name);
            role.setAdmin(name.equals("ADMINISTRATOR"));
            roleRepository.save(role);
        }
    }
}
