package bojanstipic.skeleton.users;

import bojanstipic.skeleton.TestBase;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

class ChangePasswordTests extends TestBase {

    @Autowired
    private MockMvcTester mockMvcTester;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Should change user password")
    @WithMockUser(username = "test@example.com")
    @Transactional
    void shouldChangeUserPassword() {
        entityManager.persist(
            User.builder()
                .email("test@example.com")
                .password(passwordEncoder.encode("Password123!"))
                .role(User.Role.USER)
                .build()
        );

        var req =
            """
                {
                    "oldPassword": "Password123!",
                    "newPassword": "New_pass123!"
                }
            """;

        mockMvcTester
            .put()
            .uri("/users/self/password")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(req)
            .assertThat()
            .hasStatusOk();
    }

    @Test
    @DisplayName(
        "Should fail password change if password does not match the specified criteria"
    )
    @WithMockUser
    void shouldNotChangeUserPasswordWhenBadReq() {
        var req =
            """
                {
                    "oldPassword": "pass",
                    "newPassword": "new_pass"
                }
            """;

        mockMvcTester
            .put()
            .uri("/users/self/password")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(req)
            .assertThat()
            .hasStatus(HttpStatus.BAD_REQUEST);
    }
}
