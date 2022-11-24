package bojanstipic.skeleton.security;

import bojanstipic.skeleton.users.validators.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
public class LoginReq {

    @Email
    @NotBlank
    String email;

    @Password
    String password;
}
