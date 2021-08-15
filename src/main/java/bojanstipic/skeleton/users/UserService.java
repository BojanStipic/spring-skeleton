package bojanstipic.skeleton.users;

import javax.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import bojanstipic.skeleton.users.dtos.ChangePasswordReq;
import bojanstipic.skeleton.users.dtos.LoginReq;
import bojanstipic.skeleton.users.dtos.RegisterReq;
import bojanstipic.skeleton.users.dtos.UserRes;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authManager;

    public UserRes findByEmail(String email) {
        return userRepository
            .findByEmail(email)
            .map(userMapper::map)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public boolean existsByEmail(String email) {
        return userRepository
            .existsByEmail(email);
    }

    public UserRes register(RegisterReq registerReq) {
        try {
            final var passwordHash = passwordEncoder.encode(registerReq.getPassword());
            final var request = registerReq.withPassword(passwordHash);
            final var user = userRepository.save(userMapper.map(request));
            return userMapper.map(user);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    public UserRes login(LoginReq loginReq) {
        try {
            final var auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginReq.getEmail(),
                loginReq.getPassword()
            ));
            SecurityContextHolder.getContext().setAuthentication(auth);
            return findByEmail(auth.getName());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public UserRes changePassword(String email, ChangePasswordReq changeReq) {
        final var user = userRepository
            .findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(!passwordEncoder.matches(
            changeReq.getOldPassword(),
            user.getPassword()
        )) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(changeReq.getNewPassword()));
        final var response = userRepository.save(user);

        return userMapper.map(response);
    }
}
