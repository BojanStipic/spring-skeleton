package bojanstipic.skeleton.users.dtos;

import bojanstipic.skeleton.users.User;
import lombok.Builder;
import lombok.With;

@Builder
@With
public record UserRes(
    String email,

    String name,

    String lastName,

    User.Role role
) {}
