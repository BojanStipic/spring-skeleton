package bojanstipic.skeleton.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import bojanstipic.skeleton.users.dtos.ChangePasswordReq;
import bojanstipic.skeleton.users.dtos.RegisterReq;
import bojanstipic.skeleton.users.dtos.UserRes;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Test
    public void findByEmailShouldFindUser() {
        final var user = User
            .builder()
            .id(1L)
            .email("test@test")
            .name("test")
            .lastName("test")
            .build();
        final var userRes = UserRes
            .builder()
            .email("test@test")
            .name("test")
            .lastName("test")
            .build();

        when(userRepository.findByEmail("test@test"))
            .thenReturn(Optional.of(user));
        when(userMapper.map(user)).thenReturn(userRes);

        final var result = userService.findByEmail(user.getEmail());

        assertThat(result).isEqualTo(userRes);
    }

    @Test
    public void findByEmailShouldThrowUserNotFound() {
        when(userRepository.findByEmail("test@test"))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByEmail("test@test"))
            .isInstanceOf(ResponseStatusException.class)
            .extracting("status")
            .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void registerShouldRegisterUser() {
        final var registerReq = RegisterReq
            .builder()
            .email("test@test")
            .password("1234qwerQWER")
            .build();
        final var registerReqWithHashedPassword = registerReq.withPassword(
            "password_hash"
        );
        final var user = User
            .builder()
            .email("test@test")
            .password("password_hash")
            .build();
        final var userRes = UserRes.builder().email("test@test").build();

        when(passwordEncoder.encode(registerReq.getPassword()))
            .thenReturn(registerReqWithHashedPassword.getPassword());
        when(userMapper.map(registerReqWithHashedPassword)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.map(user)).thenReturn(userRes);

        final var result = userService.register(registerReq);

        assertThat(result).isEqualTo(userRes);
    }

    @Test
    public void registerShouldThrowWhenUserAlreadyExists() {
        final var registerReq = RegisterReq
            .builder()
            .email("test@test")
            .password("1234qwerQWER")
            .build();

        when(userRepository.save(any()))
            .thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> userService.register(registerReq))
            .isInstanceOf(ResponseStatusException.class)
            .extracting("status")
            .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void changePasswordShouldChangeUserPassword() {
        final var changePasswordReq = ChangePasswordReq
            .builder()
            .oldPassword("test")
            .newPassword("new_test")
            .build();

        final var user = User
            .builder()
            .email("test@test")
            .password("password_hash")
            .build();
        final var expected = User
            .builder()
            .email("test@test")
            .password("new_password_hash")
            .build();
        final var expectedRes = UserRes.builder().email("test@test").build();

        when(userRepository.findByEmail(user.getEmail()))
            .thenReturn(Optional.of(user));
        when(
            passwordEncoder.matches(
                changePasswordReq.getOldPassword(),
                user.getPassword()
            )
        )
            .thenReturn(true);
        when(passwordEncoder.encode(changePasswordReq.getNewPassword()))
            .thenReturn(expected.getPassword());
        when(userRepository.save(expected))
            .then(AdditionalAnswers.returnsFirstArg());
        when(userMapper.map(expected)).thenReturn(expectedRes);

        final var result = userService.changePassword(
            user.getEmail(),
            changePasswordReq
        );

        assertThat(result).isEqualTo(expectedRes);
    }

    @Test
    public void changePasswordShouldThrowUserNotFound() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                userService.changePassword(
                    "test@test",
                    ChangePasswordReq.builder().build()
                )
            )
            .isInstanceOf(ResponseStatusException.class)
            .extracting("status")
            .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void changePasswordShouldThrowPasswordMismatch() {
        when(userRepository.findByEmail(any()))
            .thenReturn(Optional.of(User.builder().build()));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThatThrownBy(() ->
                userService.changePassword(
                    "test@test",
                    ChangePasswordReq.builder().build()
                )
            )
            .isInstanceOf(ResponseStatusException.class)
            .extracting("status")
            .isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
