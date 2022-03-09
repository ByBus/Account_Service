package account.presentation;

import account.auth.Role;
import account.buiseness.EventType;
import account.buiseness.Logger;
import account.buiseness.Mapper;
import account.exception.AdminLockException;
import account.exception.BadRequestException;
import account.exception.AdminDeletionException;
import account.exception.UserNotFoundException;
import account.model.dto.*;
import account.model.entity.RoleEntity;
import account.model.entity.UserEntity;
import account.repository.RepositoryService;
import account.validation.LockOperation;
import account.validation.RoleOperation;
import account.validation.UserEmailValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserManagementApiController {
    private final RepositoryService repository;
    private final Mapper<UserDTO, UserEntity> userMapper;
    private final PasswordEncoder encoder;
    private final Logger logger;

    @Autowired
    public UserManagementApiController(RepositoryService repository,
                                       Mapper<UserDTO, UserEntity> userMapper,
                                       PasswordEncoder encoder,
                                       Logger logger) {
        this.repository = repository;
        this.userMapper = userMapper;
        this.encoder = encoder;
        this.logger = logger;
    }

    @PostMapping("/api/auth/signup")
    public UserDTO getCreateUser(@Valid @RequestBody UserDTO userDTO) {
        String password = userDTO.getPassword();
        UserEntity userEntity = userMapper.mapToEntity(userDTO);
        userEntity.setPassword(encoder.encode(password));
        userEntity = repository.create(userEntity);
        logger.log(EventType.CREATE_USER, "Anonymous", userEntity.getEmail());
        return userMapper.mapToDTO(userEntity);
    }

    @PostMapping("/api/auth/changepass")
    public PasswordChangedDTO changePassword(@AuthenticationPrincipal UserDetails currentUser,
                                             @Valid @RequestBody NewPasswordDTO newPassword) {
        UserEntity user = repository.getUserByEmail(currentUser.getUsername());
        String password = newPassword.getPassword();
        if (encoder.matches(password, user.getPassword())) {
            throw new BadRequestException("The passwords must be different!");
        }
        user.setPassword(encoder.encode(password));
        repository.update(user);
        logger.log(EventType.CHANGE_PASSWORD, user.getEmail(), user.getEmail());
        return new PasswordChangedDTO(user.getEmail());
    }

    @PutMapping("/api/admin/user/role")
    public ResponseEntity<UserDTO> setRoles(@AuthenticationPrincipal UserDetails currentUser,
                                            @Valid @RequestBody RoleChangeDTO roleChangeDTO) {
        UserEntity user = repository.getUserByEmail(roleChangeDTO.getEmail());
        RoleEntity role = repository.findRole(roleChangeDTO.getRole());
        RoleOperation operation = RoleOperation.valueOf(roleChangeDTO.getOperation());
        if (operation == RoleOperation.REMOVE) {
            user.removeRole(role);
            logger.log(EventType.REMOVE_ROLE,
                    currentUser.getUsername(),
                    String.format("Remove role %s from %s", roleChangeDTO.getRole(), user.getEmail()));
        } else {
            logger.log(EventType.GRANT_ROLE,
                    currentUser.getUsername(),
                    String.format("Grant role %s to %s", roleChangeDTO.getRole(), user.getEmail()));
            user.addRole(role);
        }
        repository.update(user);
        return new ResponseEntity<>(userMapper.mapToDTO(user), HttpStatus.OK);
    }

    @DeleteMapping("/api/admin/user/{email}")
    public StatusDTO deleteUser(@AuthenticationPrincipal UserDetails currentUser,
                                @PathVariable @NotEmpty @UserEmailValid String email) {
        UserEntity user = repository.getUserByEmail(email);
        repository.deleteUser(user);
        logger.log(EventType.DELETE_USER, currentUser.getUsername(), user.getEmail());
        return new StatusDTO(email, "Deleted successfully!");
    }

    @GetMapping("/api/admin/user")
    public List<UserDTO> getUserInfo() {
        return repository.allUsers()
                .stream()
                .map(userMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/api/admin/user/access")
    public StatusDTO lockUser(@AuthenticationPrincipal UserDetails currentUser,
                              @Valid @RequestBody UserLockerDTO lockerDTO) {
        UserEntity user = repository.getUserByEmail(lockerDTO.getEmail());
        if (user == null) {
            throw new UserNotFoundException();
        }
        LockOperation operation = LockOperation.valueOf(lockerDTO.getOperation());
        switch (operation) {
            case LOCK:
                user.setFailedLoginAttempts(5);
                RoleEntity admin = repository.findRole(Role.ADMINISTRATOR);
                if (user.getRoles().contains(admin)) {
                    throw new AdminLockException();
                }
                repository.update(user);
                logger.log(EventType.LOCK_USER,
                        currentUser.getUsername(),
                        "Lock user " + user.getEmail());
                return new StatusDTO("User " + user.getEmail() + " locked!");
            case UNLOCK:
                user.setFailedLoginAttempts(0);
                repository.update(user);
                logger.log(EventType.UNLOCK_USER,
                        currentUser.getUsername(),
                        "Unlock user " + user.getEmail());
                return new StatusDTO("User " + user.getEmail() + " unlocked!");
            default:
                throw new BadRequestException("Incorrect operation");
        }
    }
}
