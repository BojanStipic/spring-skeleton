package bojanstipic.skeleton.users.dtos;

import bojanstipic.skeleton.users.validators.Password;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
public class ChangePasswordReq {

    @Password
    String oldPassword;

    @Password
    String newPassword;
}
