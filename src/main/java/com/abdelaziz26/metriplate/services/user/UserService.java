package com.abdelaziz26.metriplate.services.user;

import com.abdelaziz26.metriplate.dtos.user.AuthResponse;
import com.abdelaziz26.metriplate.dtos.user.ConfirmEmailDto;
import com.abdelaziz26.metriplate.dtos.user.LoginDto;
import com.abdelaziz26.metriplate.dtos.user.RegisterDto;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;

public interface UserService {
    Result<AuthResponse, Error> login(LoginDto loginDto, HttpServletResponse response) throws AuthenticationException;
    Result<String, Error> Register(RegisterDto registerDto) throws MessagingException;
    Result<AuthResponse, Error> refreshToken(HttpServletRequest request, HttpServletResponse response);

    Result<String, Error> sendConfirmationOtp(Map<String, String> mp) throws MessagingException;
    Result<AuthResponse, Error> confirmEmail(ConfirmEmailDto confirmEmailDto, HttpServletResponse response);
}
