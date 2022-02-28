package account.repository;

import account.exception.UserExistsException;
import account.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryService {
    private final UserRepository userRepository;

    @Autowired
    public RepositoryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isUserExist(UserEntity user) {
        return userRepository.existsByEmailIgnoreCase(user.getEmail());
    }

    public UserEntity save(UserEntity user) {
        if (isUserExist(user)) {
            throw new UserExistsException();
        }
        return userRepository.save(user);
    }

    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElse(null);
    }
}
