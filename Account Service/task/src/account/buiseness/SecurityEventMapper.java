package account.buiseness;

import account.model.dto.SecurityEventDTO;
import account.model.entity.SecurityEventEntity;
import org.springframework.stereotype.Component;

@Component
public class SecurityEventMapper implements Mapper<SecurityEventDTO, SecurityEventEntity> {
    @Override
    public SecurityEventEntity mapToEntity(SecurityEventDTO dto) {
        return new SecurityEventEntity(
                EventType.valueOf(dto.getAction()),
                dto.getSubject(),
                dto.getObject(),
                dto.getPath()
        );
    }

    @Override
    public SecurityEventDTO mapToDTO(SecurityEventEntity entity) {
        return new SecurityEventDTO(
                entity.getDate(),
                entity.getAction().name(),
                entity.getSubject(),
                entity.getObject(),
                entity.getPath()
        );
    }
}
