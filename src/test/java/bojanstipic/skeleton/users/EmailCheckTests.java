package bojanstipic.skeleton.users;

import bojanstipic.skeleton.TestBase;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

class EmailCheckTests extends TestBase {

    @Autowired
    private MockMvcTester mockMvcTester;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should return 200 when email exists")
    @Transactional
    void shouldReturn200WhenEmailExists() {
        entityManager.persist(
            User.builder()
                .email("test@example.com")
                .password("pass")
                .name("Test")
                .lastName("Test")
                .role(User.Role.USER)
                .build()
        );

        mockMvcTester
            .head()
            .uri("/users/test@example.com")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .assertThat()
            .hasStatusOk()
            .body()
            .isEmpty();
    }

    @Test
    @DisplayName("Should return 404 when email does not exist")
    void shouldReturn404WhenEmailDoesNotExist() {
        mockMvcTester
            .head()
            .uri("/users/test@example.com")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .assertThat()
            .hasStatus(HttpStatus.NOT_FOUND)
            .body()
            .isEmpty();
    }
}
