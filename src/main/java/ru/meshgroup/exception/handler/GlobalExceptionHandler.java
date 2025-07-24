package ru.meshgroup.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.meshgroup.exception.AuthenticationException;
import ru.meshgroup.exception.BalanceTransferException;
import ru.meshgroup.exception.UserNotFoundException;
import ru.meshgroup.model.response.ErrorResponse;

import javax.persistence.OptimisticLockException;
import javax.validation.ConstraintViolationException;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException exception) {
        return new ErrorResponse(MessageFormat.format("User with id {0} not found", exception.getId()));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorResponse handleAuthenticationException(AuthenticationException exception) {
        return new ErrorResponse("Authentication filed");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BalanceTransferException.class)
    public ErrorResponse handleBalanceTransferException(BalanceTransferException exception) {
        return new ErrorResponse(MessageFormat.format("Balance transfer filed with code {0}", exception.getCode()));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(OptimisticLockException.class)
    public ErrorResponse handleOptimisticLock(OptimisticLockException ex) {
        return new ErrorResponse("Data already changed");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return new ErrorResponse("VALIDATION_ERROR : " + errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.toList());

        return new ErrorResponse("VALIDATION_ERROR : " + errors);
    }
}
