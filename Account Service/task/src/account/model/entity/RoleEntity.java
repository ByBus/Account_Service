package account.model.entity;

import account.auth.Role;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {
    @Transient
    private final String PREFIX = "ROLE_";

    @Id
    @GeneratedValue
    private long id;
    private String name;
    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<UserEntity> users = new HashSet<>();
    private boolean isAdmin = false;

    public RoleEntity(Role role) {
        this.name = role.name().replace(PREFIX, "");
    }

    public String getName() {
        return PREFIX + name;
    }

    public void setAdmin(boolean value) {
        isAdmin = value;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
