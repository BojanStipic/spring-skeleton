package bojanstipic.skeleton.users;

import bojanstipic.skeleton.users.dtos.ChangePasswordReq;
import bojanstipic.skeleton.users.dtos.RegisterReq;
import bojanstipic.skeleton.users.dtos.UserRes;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public Optional<UserRes> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::map);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public UserRes register(RegisterReq registerReq) {
        final var passwordHash = passwordEncoder.encode(
            registerReq.getPassword()
        );
        final var request = registerReq.withPassword(passwordHash);
        final var user = userRepository.save(userMapper.map(request));

        return userMapper.map(user);
    }

    @Transactional
    public UserRes changePassword(String email, ChangePasswordReq changeReq) {
        final var user = userRepository.findByEmail(email).orElseThrow();

        final var isPasswordMatching = passwordEncoder.matches(
            changeReq.getOldPassword(),
            user.getPassword()
        );
        if (!isPasswordMatching) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(changeReq.getNewPassword()));

        return userMapper.map(user);
    }
}
