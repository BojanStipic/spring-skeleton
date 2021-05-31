package bojanstipic.skeleton.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import bojanstipic.skeleton.users.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException
    {
        final var user = userRepository
            .findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));

        return User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .roles(user.getRole().toString())
            .build();
    }
}
