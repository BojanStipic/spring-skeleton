package bojanstipic.skeleton.security;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationEventListener {

    @EventListener
    public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {
        final var email = (String) event.getAuthentication().getPrincipal();
        log.info("failed login attempt for user `{}`", email);
    }

    @EventListener
    public void authenticationSucceeded(AuthenticationSuccessEvent event) {
        final var principal = (User) event.getAuthentication().getPrincipal();
        final var email = principal.getUsername();
        log.info("successful login attempt for user `{}`", email);
    }
}
