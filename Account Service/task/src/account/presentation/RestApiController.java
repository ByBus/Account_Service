package account.presentation;

import account.buiseness.Mapper;
import account.exception.BadRequestException;
import account.model.NewPasswordDTO;
import account.model.PasswordChangedDTO;
import account.model.UserDTO;
import account.model.UserEntity;
import account.repository.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public UserDTO getResponse(@Valid @RequestBody UserDTO userDTO) {
        String password = userDTO.getPassword();
        UserEntity userEntity = userMapper.mapToEntity(userDTO);
        userEntity.setPassword(encoder.encode(password));
        userEntity = repository.create(userEntity);
        return userMapper.mapToDTO(userEntity);
    }

    @PostMapping("/api/auth/changepass")
    public PasswordChangedDTO changePassword(@AuthenticationPrincipal UserDetails details,
                                             @Valid @RequestBody NewPasswordDTO newPassword) {
        UserEntity user = repository.getUserByEmail(details.getUsername());
        String password = newPassword.getPassword();
        if (encoder.matches(password, user.getPassword())) {
            throw new BadRequestException("The passwords must be different!");
        }
        user.setPassword(encoder.encode(password));
        repository.update(user);
        return new PasswordChangedDTO(user.getEmail());
    }
}
