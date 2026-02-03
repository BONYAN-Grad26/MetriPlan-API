package com.abdelaziz26.metriplate.controllers;

import com.abdelaziz26.metriplate.dtos.user.AuthResponse;
import com.abdelaziz26.metriplate.dtos.user.ConfirmEmailDto;
import com.abdelaziz26.metriplate.dtos.user.LoginDto;
import com.abdelaziz26.metriplate.dtos.user.RegisterDto;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.responses.Result_.Error;

import com.abdelaziz26.metriplate.services.user.UserService;
import com.abdelaziz26.metriplate.utils._Abdel3zizController;
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
public class AuthController extends _Abdel3zizController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<@NotNull Result<String, Error>> register(@Valid @RequestBody RegisterDto registerDto) throws MessagingException {
        Result<String, Error> result = userService.Register(registerDto);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @PostMapping("/login")
    public ResponseEntity<@NotNull Result<AuthResponse, Error>> login(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
        Result<AuthResponse, Error> result = userService.login(loginDto, response);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @PostMapping("/confirm-email")
    public ResponseEntity<@NotNull Result<AuthResponse, Error>> confirmEmail(@Valid @RequestBody ConfirmEmailDto confirmEmailDto, HttpServletResponse response) {
        Result<AuthResponse, Error> result = userService.confirmEmail(confirmEmailDto, response);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<@NotNull Result<String, Error>> resendOtp(@RequestBody Map<String, String> emailMap) throws MessagingException {
        Result<String, Error> result = userService.sendConfirmationOtp(emailMap);
        return new ResponseEntity<>(result, resolveStatus(result));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<@NotNull Result<AuthResponse, Error>> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Result<AuthResponse, Error> result = userService.refreshToken(request, response);
        return new ResponseEntity<>(result, resolveStatus(result));
    }
}
