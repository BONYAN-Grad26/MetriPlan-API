package com.abdelaziz26.metriplate.services.token;

import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import com.abdelaziz26.metriplate.responses.TokenResponse;

public interface TokenService {
    TokenResponse getTokens(Long userId);
    TokenResponse refreshToken(String refreshToken);
}
