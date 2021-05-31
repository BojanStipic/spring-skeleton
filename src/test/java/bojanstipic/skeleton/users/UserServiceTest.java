package bojanstipic.skeleton.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import bojanstipic.skeleton.users.dtos.ChangePasswordReq;
import bojanstipic.skeleton.users.dtos.RegisterReq;
import bojanstipic.skeleton.users.dtos.UserRes;

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
        final var user = User.builder()
            .id(1L)
            .email("test@test")
            .name("test")
            .lastName("test")
            .build();
        final var userRes = UserRes.builder()
            .email("test@test")
            .name("test")
            .lastName("test")
            .build();

        Mockito.when(userRepository.findByEmail("test@test"))
            .thenReturn(Optional.of(user));
        Mockito.when(userMapper.map(user))
            .thenReturn(userRes);

        final var result = userService.findByEmail(user.getEmail());

        assertEquals(userRes, result);
    }

    @Test
    public void findByEmailShouldThrowUserNotFound() {
        Mockito.when(userRepository.findByEmail("test@test"))
            .thenReturn(Optional.empty());

        assertThrows(
            ResponseStatusException.class,
            () -> userService.findByEmail("test@test")
        );
    }

    @Test
    public void registerShouldRegisterUser() {
        final var registerReq = RegisterReq.builder()
            .email("test@test")
            .password("1234qwerQWER")
            .build();
        final var registerReqWithHashedPassword = registerReq.withPassword("password_hash");
        final var user = User.builder()
            .email("test@test")
            .password("password_hash")
            .build();
        final var userRes = UserRes.builder()
            .email("test@test")
            .build();

        Mockito.when(passwordEncoder.encode(registerReq.getPassword()))
            .thenReturn(registerReqWithHashedPassword.getPassword());
        Mockito.when(userMapper.map(registerReqWithHashedPassword))
            .thenReturn(user);
        Mockito.when(userRepository.save(user))
            .thenReturn(user);
        Mockito.when(userMapper.map(user))
            .thenReturn(userRes);

        final var result = userService.register(registerReq);

        assertEquals(userRes, result);
    }

    @Test
    public void registerShouldThrowWhenUserAlreadyExists() {
        final var registerReq = RegisterReq.builder()
            .email("test@test")
            .password("1234qwerQWER")
            .build();

        Mockito.when(userRepository.save(any()))
            .thenThrow(DataIntegrityViolationException.class);

        assertThrows(
            ResponseStatusException.class,
            () -> userService.register(registerReq)
        );
    }

    @Test
    public void changePasswordShouldChangeUserPassword() {
        final var changePasswordReq = ChangePasswordReq.builder()
            .oldPassword("test")
            .newPassword("new_test")
            .build();

        final var user = User.builder()
            .email("test@test")
            .password("password_hash")
            .build();
        final var expected = User.builder()
            .email("test@test")
            .password("new_password_hash")
            .build();
        final var expectedRes = UserRes.builder()
            .email("test@test")
            .build();

        Mockito.when(userRepository.findByEmail(user.getEmail()))
            .thenReturn(Optional.of(user));
        Mockito.when(
            passwordEncoder.matches(changePasswordReq.getOldPassword(), user.getPassword())
        ).thenReturn(true);
        Mockito.when(passwordEncoder.encode(changePasswordReq.getNewPassword()))
            .thenReturn(expected.getPassword());
        Mockito.when(userRepository.save(expected))
            .then(AdditionalAnswers.returnsFirstArg());
        Mockito.when(userMapper.map(expected))
            .thenReturn(expectedRes);

        final var result = userService.changePassword(user.getEmail(), changePasswordReq);

        assertEquals(expectedRes, result);
    }

    @Test
    public void changePasswordShouldThrowUserNotFound() {
        Mockito.when(userRepository.findByEmail(any()))
            .thenReturn(Optional.empty());

        assertThrows(
            ResponseStatusException.class,
            () -> userService.changePassword(
                "test@test",
                ChangePasswordReq.builder().build()
            )
        );
    }

    @Test
    public void changePasswordShouldThrowPasswordMismatch() {
        Mockito.when(userRepository.findByEmail(any()))
            .thenReturn(Optional.of(User.builder().build()));
        Mockito.when(passwordEncoder.matches(any(), any()))
            .thenReturn(false);

        assertThrows(
            ResponseStatusException.class,
            () -> userService.changePassword(
                "test@test",
                ChangePasswordReq.builder().build()
            )
        );
    }
}
