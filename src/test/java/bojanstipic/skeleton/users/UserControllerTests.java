package bojanstipic.skeleton.users;

import static org.assertj.core.api.Assertions.assertThat;

import bojanstipic.skeleton.users.User.Role;
import bojanstipic.skeleton.users.dtos.RegisterReq;
import bojanstipic.skeleton.users.dtos.UserRes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class UserControllerTests {

    @Autowired
    private UserController userController;

    @Test
    void shouldRegister() {
        var email = "new_user@example.com";
        var password = "Password1";
        var name = "Name";
        var lastName = "LastName";

        var result = userController.register(
            RegisterReq.builder()
                .email(email)
                .password(password)
                .name(name)
                .lastName(lastName)
                .build()
        );

        assertThat(result).isEqualTo(
            UserRes.builder()
                .email(email)
                .name(name)
                .lastName(lastName)
                .role(Role.USER)
                .build()
        );
    }
}
