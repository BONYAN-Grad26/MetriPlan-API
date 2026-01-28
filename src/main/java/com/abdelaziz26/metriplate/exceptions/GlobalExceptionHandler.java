package com.abdelaziz26.metriplate.exceptions;

import com.abdelaziz26.metriplate.responses.Result_.Errors;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.abdelaziz26.metriplate.responses.Result_.Error;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
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
}
