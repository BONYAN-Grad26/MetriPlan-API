package com.abdelaziz26.metriplate.services.user;

import com.abdelaziz26.metriplate.dtos.user.AuthResponse;
import com.abdelaziz26.metriplate.dtos.user.ConfirmEmailDto;
import com.abdelaziz26.metriplate.dtos.user.LoginDto;
import com.abdelaziz26.metriplate.dtos.user.RegisterDto;
import com.abdelaziz26.metriplate.entities.Role;
import com.abdelaziz26.metriplate.entities.User;
import com.abdelaziz26.metriplate.repositories.RoleRepository;
import com.abdelaziz26.metriplate.repositories.UserRepository;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Errors;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.responses.TokenResponse;
import com.abdelaziz26.metriplate.services.email.EmailService;
import com.abdelaziz26.metriplate.services.token.TokenService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${REFRESH_TOKEN_EXPIRY}")
    private String rtExpiry;

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    @Override
    public Result<AuthResponse, Error> login(LoginDto loginDto, HttpServletResponse response) throws AuthenticationException {
        Optional<User> user = userRepository.findByEmail(loginDto.getEmail());
        
        if (user.isEmpty()) {
            return Result.CreateErrorResult(Errors.NotFoundErr("User not found"));
        }

        if (!user.get().isConfirmed()) {
            return Result.CreateErrorResult(Errors.UnauthorizedErr("Please confirm your email before logging in."));
        }
        
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword() )
        );
        
        TokenResponse tokenResponse = tokenService.getTokens(user.get().getId());

        setRefreshTokenInCookie(tokenResponse.getRefreshToken(), response);
        
        return Result.CreateSuccessResult(new AuthResponse(tokenResponse));
    }

    @Override
    public Result<String, Error> Register(RegisterDto registerDto) throws MessagingException {

        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new SecurityException("No such a role Found!"));

        User user = new User();

        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setRole(role);
        user.setOtp(generateOtp());
        user.setOtpExpiry(LocalDateTime.now().plusMinutes((long) 5));

        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), user.getFirstName(), user.getOtp(), 5);

        return Result.CreateSuccessResult("User registered successfully");
    }

    @Override
    public Result<AuthResponse, Error> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String rt = getRefreshTokenFromCookie(request);

        if(rt == null) {
            return Result.CreateErrorResult(Errors.UnauthorizedErr("Refresh token not found"));
        }

        TokenResponse tokenResponse = tokenService.refreshToken(rt);

        if(!tokenResponse.isSuccess()) {
            return Result.CreateErrorResult(Errors.UnauthorizedErr("Invalid Refresh Token"));
        }

        this.setRefreshTokenInCookie(tokenResponse.getRefreshToken(), response);

        return Result.CreateSuccessResult(new AuthResponse(tokenResponse));
    }

    @Override
    public Result<String, Error> sendConfirmationOtp(HashMap<String, String> mp) throws MessagingException {
        Optional<User> user = userRepository.findByEmail(mp.get("email"));

        if (user.isEmpty()) {
            return Result.CreateErrorResult(Errors.NotFoundErr("User not found"));
        }

        if (user.get().isConfirmed()) {
            return Result.CreateErrorResult(Errors.BadRequestErr("Email is already confirmed."));
        }

        emailService.sendOtpEmail(user.get().getEmail(), user.get().getFirstName(), user.get().getOtp(), 5);
        return Result.CreateSuccessResult("OTP sent successfully");
    }

    @Override
    public Result<AuthResponse, Error> confirmEmail(ConfirmEmailDto confirmEmailDto, HttpServletResponse response) {
        Optional<User> user = userRepository.findByEmail(confirmEmailDto.getEmail());

        if (user.isEmpty()) {
            return Result.CreateErrorResult(Errors.NotFoundErr("User not found"));
        }

        if (!user.get().getOtp().equals(confirmEmailDto.getOtp())) {
            return Result.CreateErrorResult(Errors.BadRequestErr("Invalid OTP"));
        }

        if (user.get().getOtpExpiry().isBefore(LocalDateTime.now())) {
            return Result.CreateErrorResult(Errors.BadRequestErr("OTP expired"));
        }

        user.get().setConfirmed(true);
        userRepository.save(user.get());

        TokenResponse tokenResponse = tokenService.getTokens(user.get().getId());
        setRefreshTokenInCookie(tokenResponse.getRefreshToken(), response);

        return Result.CreateSuccessResult(new AuthResponse(tokenResponse));
    }

    private String generateOtp() {
        SecureRandom secureRandom = new SecureRandom();
        int otp = secureRandom.nextInt(1000000);
        return String.format("%06d", otp);
    }


    private void setRefreshTokenInCookie(String rt, HttpServletResponse response) {

        Cookie cookie =  new Cookie("refreshToken", rt);
        cookie.setPath("/");
        cookie.setMaxAge((int)(Long.parseLong(rtExpiry)));
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
    }
    private String getRefreshTokenFromCookie(HttpServletRequest request)
    {
        for(Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals("refreshToken")) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
