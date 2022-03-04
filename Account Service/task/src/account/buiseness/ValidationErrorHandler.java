package account.buiseness;


import account.model.CustomBadRequestResponseDTO;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

@ControllerAdvice
public class ValidationErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomBadRequestResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                                             HttpServletRequest request) {
        String message = getMessages(ex.getBindingResult());
        CustomBadRequestResponseDTO response = new CustomBadRequestResponseDTO(message, request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomBadRequestResponseDTO> handleConstraintNotValidException(ConstraintViolationException ex,
                                                                                         HttpServletRequest request) {

        CustomBadRequestResponseDTO response = new CustomBadRequestResponseDTO(ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private String getMessages(BindingResult bindingResult) {
        return Optional.ofNullable(bindingResult.getFieldError())
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("");
    }
}