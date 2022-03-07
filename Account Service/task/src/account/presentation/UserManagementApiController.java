package account.presentation;

import account.buiseness.Mapper;
import account.exception.BadRequestException;
import account.exception.SelfDeletionException;
import account.exception.UserNotFoundException;
import account.model.dto.*;
import account.model.entity.RoleEntity;
import account.model.entity.UserEntity;
import account.repository.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserManagementApiController {
    private final RepositoryService repository;
    private final Mapper<UserDTO, UserEntity> userMapper;
    private final PasswordEncoder encoder;

    @Autowired
    public UserManagementApiController(RepositoryService repository,
                                       Mapper<UserDTO, UserEntity> userMapper,
                                       PasswordEncoder encoder) {
        this.repository = repository;
        this.userMapper = userMapper;
        this.encoder = encoder;
    }

    @PostMapping("/api/auth/signup")
    public UserDTO getCreateUser(@Valid @RequestBody UserDTO userDTO) {
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

    @PutMapping("/api/admin/user/role")
    public ResponseEntity<UserDTO> setRoles(@Valid @RequestBody RoleChangeDTO roleChangeDTO) {
        UserEntity user = repository.getUserByEmail(roleChangeDTO.getEmail());
        RoleEntity role = repository.findRole(roleChangeDTO.getRole());
        if ("REMOVE".equals(roleChangeDTO.getOperation())) {
            user.removeRole(role);
        } else {
            user.addRole(role);
        }
        repository.update(user);
        return new ResponseEntity<>(userMapper.mapToDTO(user), HttpStatus.OK);
    }

    @DeleteMapping("/api/admin/user/{email}")
    public StatusEmailDTO deleteUser(@PathVariable String email) {
        UserEntity user = repository.getUserByEmail(email);
        RoleEntity admin = repository.findRole("ADMINISTRATOR");
        if (user == null) {
            throw new UserNotFoundException();
        }
        if (user.getRoles().contains(admin)) {
            throw new SelfDeletionException("Can't remove ADMINISTRATOR role!");
        }
        repository.deleteUser(user);
        return new StatusEmailDTO(email, "Deleted successfully!");
    }

    @GetMapping("/api/admin/user")
    public List<UserDTO> getUserInfo() {
        return repository.allUsers()
                .stream()
                .map(userMapper::mapToDTO)
                .collect(Collectors.toList());
    }

}
