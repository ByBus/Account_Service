package account.repository;

import account.model.SalaryEntity;
import account.model.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
interface SalaryRepository extends CrudRepository<SalaryEntity, Long> {

    boolean existsSalaryByUserAndPeriod(UserEntity user, LocalDate period);

    Optional<SalaryEntity> findByUserAndPeriod(UserEntity user, LocalDate period);

    List<SalaryEntity> findByUserOrderByPeriodDesc(UserEntity user);
}
