package com.abdelaziz26.metriplate.controllers;

import com.abdelaziz26.metriplate.dtos.user.AuthResponse;
import com.abdelaziz26.metriplate.dtos.user.ConfirmEmailDto;
import com.abdelaziz26.metriplate.dtos.user.LoginDto;
import com.abdelaziz26.metriplate.dtos.user.RegisterDto;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.responses.Result_.Error;

import com.abdelaziz26.metriplate.services.user.UserService;
import com.abdelaziz26.metriplate.utils._Abdel3zizController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(
        name = "01. Authentication",
        description = "Authentication and authorization endpoints"
)
public class AuthController extends _Abdel3zizController {

    private final UserService userService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account and sends an email verification OTP."
    )
    @PostMapping("/register")
    public ResponseEntity<Result<String, Error>> register(
            @Valid @RequestBody RegisterDto registerDto
    ) throws MessagingException {
        Result<String, Error> result = userService.Register(registerDto);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @Operation(
            summary = "Login",
            description = "Authenticates a user and returns access and refresh tokens."
    )
    @PostMapping("/login")
    public ResponseEntity<Result<AuthResponse, Error>> login(
            @Valid @RequestBody LoginDto loginDto,
            HttpServletResponse response
    ) {
        Result<AuthResponse, Error> result = userService.login(loginDto, response);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @Operation(
            summary = "Confirm email",
            description = "Verifies the OTP sent to the user's email and activates the account."
    )
    @PostMapping("/confirm-email")
    public ResponseEntity<Result<AuthResponse, Error>> confirmEmail(
            @Valid @RequestBody ConfirmEmailDto confirmEmailDto,
            HttpServletResponse response
    ) {
        Result<AuthResponse, Error> result = userService.confirmEmail(confirmEmailDto, response);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @Operation(
            summary = "Resend OTP",
            description = """
                Sends a new email verification OTP.

                Request body must contain a JSON object with a single key named 'email'.
                """
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = """
                Example:
                {
                  "email": "mohamed@example.com"
                }
                """,
            required = true
    )
    @PostMapping("/resend-otp")
    public ResponseEntity<Result<String, Error>> resendOtp(
            @RequestBody Map<String, String> emailMap
    ) throws MessagingException {
        Result<String, Error> result = userService.sendConfirmationOtp(emailMap);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token using a valid refresh token extracted from http-only-cookie."
    )
    @PostMapping("/refresh-token")
    public ResponseEntity<Result<AuthResponse, Error>> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Result<AuthResponse, Error> result = userService.refreshToken(request, response);
        return new ResponseEntity<>(result, resolveStatus(result));
    }
}
