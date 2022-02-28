package account.buiseness;

import account.model.UserDTO;
import account.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<UserDTO, UserEntity> {
    @Override
    public UserEntity mapToEntity(UserDTO dto) {
        return new UserEntity(dto.getName(),
                dto.getLastname(),
                dto.getEmail().toLowerCase(),
                dto.getPassword());
    }

    @Override
    public UserDTO mapToDTO(UserEntity entity) {
        return new UserDTO(entity.getId(),
                entity.getName(),
                entity.getLastname(),
                entity.getEmail(),
                entity.getPassword());
    }
}
