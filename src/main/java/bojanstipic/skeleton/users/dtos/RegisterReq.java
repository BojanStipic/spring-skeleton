package bojanstipic.skeleton.users.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
public class RegisterReq {

    @Email
    @NotBlank
    String email;

    @Size(min = 8)
    @Pattern(regexp = "(?U)^(?=.*\\p{Lower})(?=.*\\p{Upper})(?=.*\\d).+$")
    @NotBlank
    String password;

    @Pattern(regexp = "(?U)\\p{Alpha}*")
    String name;

    @Pattern(regexp = "(?U)\\p{Alpha}*")
    String lastName;
}
