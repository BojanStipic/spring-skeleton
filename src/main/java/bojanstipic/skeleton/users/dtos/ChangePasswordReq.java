package bojanstipic.skeleton.users.dtos;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
public class ChangePasswordReq {

    @Size(min = 8)
    @Pattern(regexp = "(?U)^(?=.*\\p{Lower})(?=.*\\p{Upper})(?=.*\\d).+$")
    String oldPassword;

    @Size(min = 8)
    @Pattern(regexp = "(?U)^(?=.*\\p{Lower})(?=.*\\p{Upper})(?=.*\\d).+$")
    String newPassword;
}
