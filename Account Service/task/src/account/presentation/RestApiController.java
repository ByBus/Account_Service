package account.presentation;

import account.buiseness.Mapper;
import account.model.UserDTO;
import account.model.UserEntity;
import account.repository.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RestApiController {

    private final RepositoryService repository;
    private final Mapper<UserDTO, UserEntity> userMapper;
    private final PasswordEncoder encoder;

    @Autowired
    public RestApiController(RepositoryService repository,
                             Mapper<UserDTO, UserEntity> userMapper,
                             PasswordEncoder encoder) {
        this.repository = repository;
        this.userMapper = userMapper;
        this.encoder = encoder;
    }

    @PostMapping("/api/auth/signup")
    public UserDTO getResponse(@Valid @RequestBody UserDTO user) {
        UserEntity userEntity = userMapper.mapToEntity(user);
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        UserEntity newUser = repository.save(userEntity);
        return userMapper.mapToDTO(newUser);
    }

    @GetMapping("/api/empl/payment")
    public UserDTO getAuthenticatedUser(@AuthenticationPrincipal UserDetails details) {
        UserEntity user = repository.getUserByEmail(details.getUsername());
        return userMapper.mapToDTO(user);
    }
}
