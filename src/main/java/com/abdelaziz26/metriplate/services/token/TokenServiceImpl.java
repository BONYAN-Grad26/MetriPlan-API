package com.abdelaziz26.metriplate.services.token;

import com.abdelaziz26.metriplate.entities.RefreshToken;
import com.abdelaziz26.metriplate.entities.User;
import com.abdelaziz26.metriplate.repositories.RefreshTokenRepository;
import com.abdelaziz26.metriplate.repositories.UserRepository;
import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Errors;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.responses.TokenResponse;
import com.abdelaziz26.metriplate.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    @Value(("${REFRESH_TOKEN_EXPIRY}"))
    private String refreshTokenExpiry;

    @Value(("${JWT_EXPIRY}"))
    private String accessTokenExpiry;


    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Override
    public TokenResponse getTokens(Long userId) {

        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty())
            return TokenResponse.builder()
                    .success(false)
                    .build();

        String accessToken = jwtService.generateToken(user.get());
        RefreshToken rt = createRefreshToken(user.get());

        refreshTokenRepository.save(rt);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .accessTokenExpiresIn(LocalDateTime.now().plus(Long.parseLong(accessTokenExpiry), ChronoUnit.MILLIS))
                .refreshToken(rt.getToken())
                .refreshTokenExpiresIn(rt.getExpiresAt())
                .success(true)
                .build();
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {

        RefreshToken rt = validateRefreshToken(refreshToken);

        if(rt == null)
            return TokenResponse.builder()
                    .success(false)
                    .build();

        rt.setRevoked(true);

        refreshTokenRepository.save(rt);

        return getTokens(rt.getUser().getId());
    }

    private RefreshToken validateRefreshToken(String refreshToken) {

        Optional<RefreshToken> rt = refreshTokenRepository.findByToken(refreshToken);

        boolean valid = rt.isPresent() && !rt.get().isRevoked() && !rt.get().getExpiresAt().isBefore(LocalDateTime.now());

        return valid ? rt.get() : null;
    }

    private RefreshToken createRefreshToken(User user) {
        return RefreshToken.builder()
                .token(generateRefreshToken())
                .expiresAt(LocalDateTime.now().plus(Long.parseLong(refreshTokenExpiry), ChronoUnit.MILLIS))
                .revoked(false)
                .user(user)
                .build();
    }

    private String generateRefreshToken() {
        return UUID.randomUUID().toString() + '_' +  System.currentTimeMillis() + '_' + UUID.randomUUID().toString();
    }
}
