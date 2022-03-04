package account.model;

import account.validation.PasswordNotBreached;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
    private long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String lastname;
    @NotEmpty
    @Email(regexp = "\\w+@acme.com")
    private String email;
    @NotEmpty
    @PasswordNotBreached
    @Size(min = 12, message = "Password length must be 12 chars minimum!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
