package account.repository;

import account.exception.BadRequestException;
import account.exception.DuplicateSalaryException;
import account.exception.UserExistsException;
import account.model.SalaryEntity;
import account.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RepositoryService {
    private final UserRepository userRepository;
    private final SalaryRepository salaryRepository;

    @Autowired
    public RepositoryService(UserRepository userRepository,
                             SalaryRepository salaryRepository) {
        this.userRepository = userRepository;
        this.salaryRepository = salaryRepository;
    }

    public boolean isUserExist(UserEntity user) {
        return userRepository.existsByEmailIgnoreCase(user.getEmail());
    }

    public UserEntity create(UserEntity user) {
        if (isUserExist(user)) {
            throw new UserExistsException();
        }
        return userRepository.save(user);
    }

    public UserEntity update(UserEntity user) {
        return userRepository.save(user);
    }

    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElse(null);
    }

    public void addAll(List<SalaryEntity> salaries) {
        salaryRepository.saveAll(salaries);
    }

    public SalaryEntity update(SalaryEntity salary) {
        SalaryEntity oldSalary = getSalary(salary.getUser(), salary.getPeriod());
        if (oldSalary == null) {
            throw new BadRequestException("Can't update!");
        }
        salary.setId(oldSalary.getId());
        return salaryRepository.save(salary);
    }

    public SalaryEntity getSalary(UserEntity user, LocalDate date) {
        return salaryRepository.findByUserAndPeriod(user, date).orElse(null);
    }

    public List<SalaryEntity> getSalariesSorted(UserEntity user) {
        return salaryRepository.findByUserOrderByPeriodDesc(user);
    }

    @Transactional(rollbackFor = { DuplicateSalaryException.class })
    public void saveAll(List<SalaryEntity> salaries){
        for (var salary : salaries) {
            if (salaryRepository.existsSalaryByUserAndPeriod(salary.getUser(), salary.getPeriod())) {
                throw new DuplicateSalaryException();
            }
            salaryRepository.save(salary);
        }
    }
}
