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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

class LoginTests extends TestBase {

    @Autowired
    private MockMvcTester mockMvcTester;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Should login existing user")
    @Transactional
    void shouldLogin() {
        entityManager.persist(
            User.builder()
                .email("test@example.com")
                .password(passwordEncoder.encode("Password123!"))
                .name("Test")
                .lastName("Test")
                .role(User.Role.USER)
                .build()
        );

        var req =
            """
                {
                    "email": "test@example.com",
                    "password": "Password123!"
                }
            """;

        mockMvcTester
            .post()
            .uri("/login")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(req)
            .assertThat()
            .hasStatusOk();
    }

    @Test
    @DisplayName("Should not login when passwords do not match")
    @Transactional
    void shouldNotLoginWhenPassDoesNotMatch() {
        entityManager.persist(
            User.builder()
                .email("test@example.com")
                .password(passwordEncoder.encode("Password123!"))
                .name("Test")
                .lastName("Test")
                .role(User.Role.USER)
                .build()
        );

        var req =
            """
                {
                    "email": "test@example.com",
                    "password": "WrongPass123!"
                }
            """;

        mockMvcTester
            .post()
            .uri("/login")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(req)
            .assertThat()
            .hasStatus(HttpStatus.BAD_REQUEST);
    }
}
