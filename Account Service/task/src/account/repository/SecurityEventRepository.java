package account.repository;

import account.model.entity.SecurityEventEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecurityEventRepository extends CrudRepository<SecurityEventEntity, Long> {
    List<SecurityEventEntity> findAll();
}
