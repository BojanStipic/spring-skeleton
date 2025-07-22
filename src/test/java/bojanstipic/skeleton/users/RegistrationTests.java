package bojanstipic.skeleton.users;

import bojanstipic.skeleton.TestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

class RegistrationTests extends TestBase {

    @Autowired
    private MockMvcTester mockMvcTester;

    @Test
    @DisplayName("Should register a new user")
    void shouldRegister() {
        var req = """
            {
                "email": "new_user@example.com",
                "password": "Password1",
                "name": "Name",
                "lastName": "LastName"
            }
            """;

        var expectedRes = """
            {
                "email": "new_user@example.com",
                "name": "Name",
                "lastName": "LastName",
                "role": "USER"
            }
            """;

        mockMvcTester
            .post()
            .uri("/users")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(req)
            .assertThat()
            .hasStatusOk()
            .bodyJson()
            .isLenientlyEqualTo(expectedRes);
    }

    @Test
    @DisplayName("Should return 400 when email is not specified")
    void shouldReturn400WhenEmailIsNotSpecified() {
        var req = """
            {
                "password": "Password1",
                "name": "Name",
                "lastName": "LastName"
            }
            """;

        mockMvcTester
            .post()
            .uri("/users")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(req)
            .assertThat()
            .hasStatus(HttpStatus.BAD_REQUEST);
    }
}
