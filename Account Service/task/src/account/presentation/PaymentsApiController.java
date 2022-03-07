package account.presentation;


import account.buiseness.Mapper;
import account.buiseness.StringFormatter;
import account.model.dto.SalaryDTO;
import account.model.entity.SalaryEntity;
import account.model.dto.StatusDTO;
import account.model.entity.UserEntity;
import account.repository.RepositoryService;
import account.validation.DateValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
public class PaymentsApiController {
    private final RepositoryService repository;
    private final StringFormatter<LocalDate> dateFormatter;
    private final Mapper<SalaryDTO, SalaryEntity> salaryMapper;

    @Autowired
    public PaymentsApiController(RepositoryService repository,
                                 StringFormatter<LocalDate> dateFormatter,
                                 Mapper<SalaryDTO, SalaryEntity> salaryMapper) {
        this.repository = repository;
        this.dateFormatter = dateFormatter;
        this.salaryMapper = salaryMapper;
    }

    @GetMapping("/api/empl/payment")
    public ResponseEntity<?> getSalaryForPeriod(@AuthenticationPrincipal UserDetails details,
                                                @RequestParam(required = false) @DateValidation String period) {
        UserEntity user = repository.getUserByEmail(details.getUsername());
        if (period == null) {
            List<SalaryEntity> salaries = repository.getSalariesSorted(user);
            return new ResponseEntity<>(salaries.stream()
                    .map(salaryMapper::mapToDTO)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } else {
            LocalDate periodFormatted = dateFormatter.format(period);
            SalaryEntity salary = repository.getSalary(user, periodFormatted);
            return new ResponseEntity<>(salaryMapper.mapToDTO(salary), HttpStatus.OK);
        }
    }

    @PostMapping("/api/acct/payments")
    public ResponseEntity<StatusDTO> saveUsersSalaries(@RequestBody
                                                       @NotEmpty(message = "Input salary list can't be empty.")
                                                               List<@Valid SalaryDTO> salaries) {
        List<SalaryEntity> salaryEntities = salaries.stream()
                .map(salaryMapper::mapToEntity)
                .collect(Collectors.toList());
        repository.saveAll(salaryEntities);
        return new ResponseEntity<>(new StatusDTO("Added successfully!"), HttpStatus.OK);
    }

    @PutMapping("/api/acct/payments")
    public ResponseEntity<StatusDTO> updateSalary(@Valid @RequestBody SalaryDTO salaryDTO) {
        SalaryEntity salaryEntity = salaryMapper.mapToEntity(salaryDTO);
        repository.update(salaryEntity);
        return new ResponseEntity<>(new StatusDTO("Updated successfully!"), HttpStatus.OK);
    }
}
