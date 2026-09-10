package dev.hollandwesley.avarra.auth.api;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.hollandwesley.avarra.auth.application.UsernameAlreadyExistsException;

/**
 * Translates authentication, registration, and request-validation failures
 * into consistent HTTP API error responses.
 * 
 * <p>This advice keeps HTTP-specific error handling at the authentication API
 * boundary rather than coupling application or security code to HTTP responses.
 */
@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleUsernameAlreadyExists(
            UsernameAlreadyExistsException exception
    ) {
        return new ApiError(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                "Username is already in use"
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationFailure(
            MethodArgumentNotValidException exception
    ) {
        return new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Request is invalid"
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleAuthenticationFailure(
            AuthenticationException exception
    ) {
        return new ApiError(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Invalid username or password"
        );
    }
}