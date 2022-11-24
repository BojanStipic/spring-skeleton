package bojanstipic.skeleton.users;

import bojanstipic.skeleton.users.dtos.ChangePasswordReq;
import bojanstipic.skeleton.users.dtos.RegisterReq;
import bojanstipic.skeleton.users.dtos.UserRes;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserRes register(@RequestBody @Valid RegisterReq registerReq) {
        return userService.register(registerReq);
    }

    @RequestMapping(method = RequestMethod.HEAD, value = "/{email}")
    public ResponseEntity<Void> checkEmailExists(@PathVariable String email) {
        if (userService.existsByEmail(email)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public UserRes getAuthenticatedUser(Principal principal) {
        return userService.findByEmail(principal.getName()).orElseThrow();
    }

    @PutMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    public UserRes changePassword(
        Principal principal,
        @RequestBody @Valid ChangePasswordReq changeReq
    ) {
        return userService.changePassword(principal.getName(), changeReq);
    }
}
