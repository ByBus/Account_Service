package account.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "salary")
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class SalaryEntity {
    @Id
    @GeneratedValue
    private long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity user;

    @NonNull
    private LocalDate period;
    @NonNull
    private long salaryCents;
}
