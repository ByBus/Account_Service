package account.model.entity;

import account.buiseness.EventType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "security_event")
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class SecurityEventEntity {
    @Id
    @GeneratedValue
    private long id;
    @CreationTimestamp
    private LocalDateTime date;
    @NonNull
    @Enumerated(EnumType.STRING)
    private EventType action;
    @NonNull
    private String subject;
    @NonNull
    private String object;
    @NonNull
    private String path;
}
