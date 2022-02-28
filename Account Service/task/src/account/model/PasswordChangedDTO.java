package account.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class PasswordChangedDTO {
    @NonNull
    private String email;
    private String status = "The password has been updated successfully";
}
