package account.repository;

import account.exception.BadRequestException;
import account.exception.DuplicateSalaryException;
import account.exception.UserExistsException;
import account.model.entity.RoleEntity;
import account.model.entity.SalaryEntity;
import account.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RepositoryService {
    private final UserRepository userRepository;
    private final SalaryRepository salaryRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public RepositoryService(UserRepository userRepository,
                             SalaryRepository salaryRepository,
                             RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.salaryRepository = salaryRepository;
        this.roleRepository = roleRepository;
    }

    public boolean isUserExist(UserEntity user) {
        return userRepository.existsByEmailIgnoreCase(user.getEmail());
    }

    public UserEntity create(UserEntity user) {
        if (isUserExist(user)) {
            throw new UserExistsException();
        }
        user.addRole(roleRepository.findByName(countUsers() == 0 ? "ADMINISTRATOR" : "USER"));
        return userRepository.save(user);
    }

    public UserEntity update(UserEntity user) {
        return userRepository.save(user);
    }

    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElse(null);
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

    public long countUsers() {
        return userRepository.count();
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

    public void deleteUser(UserEntity user) {
        userRepository.delete(user);
    }

    public List<UserEntity> allUsers() {
        return userRepository.findAll();
    }

    public RoleEntity findRole(String name) {
        return roleRepository.findByName(name.replace("ROLE_", ""));
    }
}
