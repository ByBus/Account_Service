package account.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StatusEmailDTO {
    @JsonProperty("user")
    private String email;
    private String status;
}
