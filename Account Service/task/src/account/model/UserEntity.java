package account.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class UserEntity {
    @Id
    @GeneratedValue
    private long id;
    @NonNull
    private String name;
    @NonNull
    private String lastname;
    @NonNull
    private String email;
    @NonNull
    private String password;

    private String role = "ROLE_USER";

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SalaryEntity> salary = new ArrayList<>();
}