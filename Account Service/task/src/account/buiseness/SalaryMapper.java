package account.buiseness;

import account.model.SalaryDTO;
import account.model.SalaryEntity;
import account.model.UserEntity;
import account.repository.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SalaryMapper implements Mapper<SalaryDTO, SalaryEntity> {
    RepositoryService repository;
    StringFormatter<Long> salaryFormatter;
    StringFormatter<LocalDate> dateFormatter;

    @Autowired
    public SalaryMapper(RepositoryService repository,
                        StringFormatter<Long> salaryFormatter,
                        StringFormatter<LocalDate> dateFormatter) {
        this.repository = repository;
        this.salaryFormatter = salaryFormatter;
        this.dateFormatter = dateFormatter;
    }

    @Override
    public SalaryEntity mapToEntity(SalaryDTO dto) {
        UserEntity user = repository.getUserByEmail(dto.getEmail());

        return new SalaryEntity(user,
                dateFormatter.format(dto.getPeriod()),
                dto.getSalaryCents());
    }

    @Override
    public SalaryDTO mapToDTO(SalaryEntity entity) {
        return new SalaryDTO(entity.getUser().getName(),
                entity.getUser().getLastname(),
                dateFormatter.formatToString(entity.getPeriod()),
                salaryFormatter.formatToString(entity.getSalaryCents()));
    }
}
