package bojanstipic.skeleton.users.dtos;

import bojanstipic.skeleton.users.User;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
public class UserRes {

    String email;

    String name;

    String lastName;

    User.Role role;
}
