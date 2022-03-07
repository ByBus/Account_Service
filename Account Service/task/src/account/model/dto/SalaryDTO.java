package account.model.dto;

import account.validation.DateValidation;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;
import java.util.Objects;

@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Getter
public class SalaryDTO {
    @JsonProperty(value = "employee", access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(message = "email must not be empty")
    @Email(regexp = "\\w+@acme.com", message = "email must end with @acme.com")
    private String email;
    
    private final String name;
    private final String lastname;

    @NotEmpty
    @DateValidation
    private final String period;

    @JsonProperty(value = "salary", access = JsonProperty.Access.READ_ONLY)
    private final String salaryFormatted;

    @Min(value = 0, message = "salary must be positive number")
    @JsonProperty(value = "salary", access = JsonProperty.Access.WRITE_ONLY)
    private long salaryCents;
}