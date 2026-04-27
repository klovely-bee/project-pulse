package team.projectpulse.system;

import team.projectpulse.shared.exception.ConflictException;
import team.projectpulse.shared.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<Result> handleNotFound(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Result.failure(StatusCode.NOT_FOUND, exception.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    ResponseEntity<Result> handleConflict(ConflictException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Result.failure(StatusCode.CONFLICT, exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Result> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(error -> error.getField() + " " + error.getDefaultMessage())
            .orElse("Validation failed");

        return ResponseEntity.badRequest()
            .body(Result.failure(StatusCode.INVALID_ARGUMENT, message));
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<Result> handleForbidden(AccessDeniedException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(Result.failure(StatusCode.FORBIDDEN, exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Result> handleGeneric(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Result.failure(StatusCode.INTERNAL_SERVER_ERROR, exception.getMessage()));
    }
}
