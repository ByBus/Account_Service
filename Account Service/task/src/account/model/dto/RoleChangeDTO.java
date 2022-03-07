package account.model.dto;

import account.validation.RoleChangeCorrect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Data
@RoleChangeCorrect
public class RoleChangeDTO {
    @NotEmpty
    @JsonProperty("user")
    private String email;
    @NotEmpty
    private String role;
    @NotEmpty
    private String operation;
}
