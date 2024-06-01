package bojanstipic.skeleton.users;

import bojanstipic.skeleton.users.dtos.RegisterReq;
import bojanstipic.skeleton.users.dtos.UserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    User map(RegisterReq registerReq) {
        return User.builder()
            .email(registerReq.email())
            .password(registerReq.password())
            .name(registerReq.name())
            .lastName(registerReq.lastName())
            .build();
    }

    UserRes map(User user) {
        return UserRes.builder()
            .email(user.getEmail())
            .name(user.getName())
            .lastName(user.getLastName())
            .role(user.getRole())
            .build();
    }
}
