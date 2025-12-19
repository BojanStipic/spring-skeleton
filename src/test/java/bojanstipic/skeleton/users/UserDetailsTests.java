package bojanstipic.skeleton.users;

import bojanstipic.skeleton.TestBase;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

class UserDetailsTests extends TestBase {

    @Autowired
    private MockMvcTester mockMvcTester;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should return user details")
    @WithMockUser(username = "test@example.com")
    @Transactional
    void shouldReturnUserDetails() {
        entityManager.persist(
            User.builder()
                .email("test@example.com")
                .password("pass")
                .name("Test")
                .lastName("Test")
                .role(User.Role.USER)
                .build()
        );

        var expectedRes = """
            {
                "email": "test@example.com",
                "name": "Test",
                "lastName": "Test",
                "role": "USER"
            }
            """;

        mockMvcTester
            .get()
            .uri("/users/self")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .assertThat()
            .hasStatusOk()
            .bodyJson()
            .isLenientlyEqualTo(expectedRes);
    }

    @Test
    @DisplayName("Should return 401 when unauthenticated")
    void shouldReturn401WhenUnauthenticated() {
        mockMvcTester
            .get()
            .uri("/users/self")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .assertThat()
            .hasStatus(HttpStatus.UNAUTHORIZED);
    }
}
