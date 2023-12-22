package bojanstipic.skeleton.users.dtos;

import bojanstipic.skeleton.users.validators.Password;
import lombok.Builder;
import lombok.With;

@Builder
@With
public record ChangePasswordReq(
    @Password String oldPassword,

    @Password String newPassword
) {}
