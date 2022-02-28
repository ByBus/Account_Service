package account.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class CustomBadRequestResponseDTO {
    private LocalDateTime timestamp = LocalDateTime.now();
    private int status = HttpStatus.BAD_REQUEST.value();
    private String error = "Bad Request";
    private final String message;
    private final String path;
}
