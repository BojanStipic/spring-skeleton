package bojanstipic.skeleton.users.dtos;

import bojanstipic.skeleton.users.validators.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.With;

@Builder
@With
public record RegisterReq(
    @Email @NotBlank String email,

    @Password String password,

    @Pattern(regexp = "(?U)\\p{Alpha}*") String name,

    @Pattern(regexp = "(?U)\\p{Alpha}*") String lastName
) {}
