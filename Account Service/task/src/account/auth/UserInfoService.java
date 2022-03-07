package account.auth;

import account.model.entity.UserEntity;
import account.repository.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements UserDetailsService {
    @Autowired
    RepositoryService repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = repository.getUserByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Not found: " + email);
        }

        return new UserInfo(user);
    }
}