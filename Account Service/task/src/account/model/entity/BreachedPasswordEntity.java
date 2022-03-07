package account.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "breached_password")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BreachedPasswordEntity {
    @Id
    private String password;
}
