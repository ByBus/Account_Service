package account.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class StatusDTO {
    @JsonProperty("user")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    @NonNull
    private String status;
}
