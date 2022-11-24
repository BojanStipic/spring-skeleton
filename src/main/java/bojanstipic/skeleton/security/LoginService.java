package bojanstipic.skeleton.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authManager;

    private final SecurityContextRepository securityContextRepository;

    public void login(
        LoginReq loginReq,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        final var auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginReq.getEmail(),
                loginReq.getPassword()
            )
        );

        final var context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);
        securityContextRepository.saveContext(context, request, response);
    }
}
