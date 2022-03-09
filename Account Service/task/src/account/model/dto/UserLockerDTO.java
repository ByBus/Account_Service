package account.model.dto;

import account.validation.LockOperation;
import account.validation.OperationValid;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserLockerDTO {
    @JsonProperty("user")
    @NotEmpty(message = "User must not be empty")
    private String email;
    @OperationValid(enumClazz = LockOperation.class, message = "Operation must be LOCK or UNLOCK!")
    private String operation;
}
