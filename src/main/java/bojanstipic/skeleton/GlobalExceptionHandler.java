package bojanstipic.skeleton;

import jakarta.validation.ConstraintViolationException;
import java.util.NoSuchElementException;
import java.util.Optional;
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
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            Optional.ofNullable(e.getMessage()).orElse(
                "The requested resource was not found."
            )
        );
    }

    @ExceptionHandler
    public ProblemDetail handle(ConstraintViolationException e) {
        var problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            Optional.ofNullable(e.getMessage()).orElse("Validation failed.")
        );

        record ConstraintError(String field, String message, String code) {}

        problemDetail.setProperty(
            "errors",
            e
                .getConstraintViolations()
                .stream()
                .map(violation ->
                    new ConstraintError(
                        violation.getPropertyPath().toString(),
                        violation.getMessage(),
                        violation
                            .getConstraintDescriptor()
                            .getAnnotation()
                            .annotationType()
                            .getSimpleName()
                    )
                )
                .toList()
        );

        return problemDetail;
    }

    @ExceptionHandler
    public ProblemDetail handle(DataIntegrityViolationException e) {
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT,
            "Request conflicts with existing data or database constraints."
        );
    }

    @ExceptionHandler
    public ProblemDetail handle(AuthenticationException e) {
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            Optional.ofNullable(e.getMessage()).orElse(
                "Invalid authentication credentials."
            )
        );
    }

    @ExceptionHandler
    public ProblemDetail handle(AccessDeniedException e) {
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.FORBIDDEN,
            Optional.ofNullable(e.getMessage()).orElse(
                "You do not have permission to perform this action."
            )
        );
    }

    @ExceptionHandler
    public ProblemDetail handle(Throwable e) {
        log.error("Internal server error", e);
        return ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
