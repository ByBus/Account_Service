package account.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
}