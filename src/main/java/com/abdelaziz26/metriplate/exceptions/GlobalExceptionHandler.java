package com.abdelaziz26.metriplate.exceptions;

import com.abdelaziz26.metriplate.responses.Result_.Errors;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.abdelaziz26.metriplate.responses.Result_.Error;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<@NotNull Result<Object, Error>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        StringBuilder errorsAsString = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        for (Map.Entry<String, String> error : errors.entrySet()) {
            errorsAsString.append(error.getKey()).append(": ").append(error.getValue()).append("\n");
        }

        Result<Object, Error> result = Result.CreateErrorResult(Errors.ValidationErr(errorsAsString.toString()));
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<@NotNull Result<Object, Error>> handleMessagingException(MessagingException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(Result.CreateErrorResult(Errors.InternalServerErr(ex.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PlanGenerationException.class)
    public ResponseEntity<@NotNull Result<Object, Error>> handlePlanGenerationException(PlanGenerationException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(Result.CreateErrorResult(Errors.InternalServerErr(ex.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<@NotNull Result<Object, Error>> handleAuthenticationException(AuthenticationException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(Result.CreateErrorResult(Errors.BadRequestErr(ex.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<@NotNull Result<Object, Error>> handleNullPointerException(NullPointerException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(Result.CreateErrorResult(Errors.InternalServerErr(ex.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NotNull Result<Object, Error>> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(Result.CreateErrorResult(Errors.InternalServerErr(ex.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
