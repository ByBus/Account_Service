package account.repository;

import account.auth.Role;
import account.exception.BadRequestException;
import account.exception.DuplicateSalaryException;
import account.exception.UserExistsException;
import account.model.entity.RoleEntity;
import account.model.entity.SalaryEntity;
import account.model.entity.SecurityEventEntity;
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
    private final SecurityEventRepository eventRepository;

    @Autowired
    public RepositoryService(UserRepository userRepository,
                             SalaryRepository salaryRepository,
                             RoleRepository roleRepository,
                             SecurityEventRepository eventRepository) {
        this.userRepository = userRepository;
        this.salaryRepository = salaryRepository;
        this.roleRepository = roleRepository;
        this.eventRepository = eventRepository;
    }

    public boolean isUserExist(UserEntity user) {
        return userRepository.existsByEmailIgnoreCase(user.getEmail());
    }

    public UserEntity create(UserEntity user) {
        if (isUserExist(user)) {
            throw new UserExistsException();
        }
        user.addRole(findRole(countUsers() == 0 ? Role.ADMINISTRATOR : Role.USER));
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

    public RoleEntity findRole(Role role) {
        return findRole(role.name());
    }
    public RoleEntity findRole(String name) {
        return roleRepository.findByName(name.replace("ROLE_", ""));
    }

    public List<SecurityEventEntity> getAllEvents() {
        return eventRepository.findAll();
    }
}
