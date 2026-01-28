package com.abdelaziz26.metriplate.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {

    private String accessToken;

    private String refreshToken;

    private LocalDateTime accessTokenExpiresIn;

    private LocalDateTime refreshTokenExpiresIn;

    private boolean success;
}
