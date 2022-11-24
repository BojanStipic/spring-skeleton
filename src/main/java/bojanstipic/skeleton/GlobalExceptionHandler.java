package bojanstipic.skeleton;

import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ProblemDetail handle(NoSuchElementException e) {
        return ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ProblemDetail handle(DataIntegrityViolationException e) {
        return ProblemDetail.forStatus(HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ProblemDetail handle(AuthenticationException e) {
        return ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ProblemDetail handle(AccessDeniedException e) {
        return ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ProblemDetail handle(Throwable e) {
        log.error("Internal server error", e);
        return ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
