package account;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RestApiController {

    @PostMapping("api/auth/signup")
    public UserDTO getResponse(@Valid @RequestBody UserDTO user) {
        return user;
    }
}
