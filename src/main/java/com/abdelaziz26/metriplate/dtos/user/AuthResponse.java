package com.abdelaziz26.metriplate.dtos.user;

import com.abdelaziz26.metriplate.responses.TokenResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private LocalDateTime expiresIn;

    public AuthResponse(TokenResponse tokenResponse) {
        this.accessToken = tokenResponse.getAccessToken();
        this.expiresIn = tokenResponse.getAccessTokenExpiresIn();
    }
}
