package account.repository;

import account.model.entity.BreachedPasswordEntity;
import org.springframework.data.repository.CrudRepository;

public interface BreachedPasswordRepository extends CrudRepository<BreachedPasswordEntity, String> {
}
