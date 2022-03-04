package account.model;

import account.validation.PasswordNotBreached;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewPasswordDTO {
    @Size(min = 12, message = "Password length must be 12 chars minimum!")
    @NotEmpty
    @PasswordNotBreached
    @JsonProperty("new_password")
    private String password;
}
