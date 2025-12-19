package bojanstipic.skeleton.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    @PostMapping
    public void login(
        @RequestBody @Valid LoginReq loginReq,
        HttpServletRequest request
    ) {
        try {
            request.login(loginReq.email(), loginReq.password());
        } catch (ServletException _) {
            throw new BadCredentialsException("Bad credentials");
        }
    }
}
