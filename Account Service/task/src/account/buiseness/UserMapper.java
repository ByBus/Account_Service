package account.buiseness;

import account.model.entity.RoleEntity;
import account.model.dto.UserDTO;
import account.model.entity.UserEntity;
import account.repository.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Component
public class UserMapper implements Mapper<UserDTO, UserEntity> {
    @Autowired
    RepositoryService repository;

    @Override
    public UserEntity mapToEntity(UserDTO dto) {
        UserEntity userEntity = new UserEntity(dto.getName(),
                dto.getLastname(),
                dto.getEmail().toLowerCase(),
                dto.getPassword());
        if (dto.getRoles() != null) {
            var roles = dto.getRoles().stream()
                    .map(repository::findRole)
                    .collect(Collectors.toList());
            userEntity.getRoles().addAll(roles);
        }
        return userEntity;
    }

    @Override
    public UserDTO mapToDTO(UserEntity entity) {
        Set<String> roles = entity.getRoles().stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toCollection(TreeSet::new));
        return new UserDTO(entity.getId(),
                entity.getName(),
                entity.getLastname(),
                entity.getEmail(),
                entity.getPassword(),
                roles);
    }
}
