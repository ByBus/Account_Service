package account.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SecurityEventDTO {
    private LocalDateTime date;
    private String action;
    private String subject;
    private String object;
    private String path;
}
