package com.abdelaziz26.metriplate.security;

import com.abdelaziz26.metriplate.entities.user.Role;
import com.abdelaziz26.metriplate.entities.user.User;
import com.abdelaziz26.metriplate.repositories.RoleRepository;
import com.abdelaziz26.metriplate.repositories.UserRepository;
import com.abdelaziz26.metriplate.responses.TokenResponse;
import com.abdelaziz26.metriplate.services.token.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    final UserRepository userRepository;
    final RoleRepository roleRepository;
    final TokenService   tokenService;
    static String GOOGLE_OAUTH_1ST_NAME_KEY = "given_name";
    static String GOOGLE_OAUTH_2ND_NAME_KEY = "family_name";

    @Value("${REFRESH_TOKEN_EXPIRY}")
    String rtExpiry;


    @Override
    public void onAuthenticationSuccess(@NotNull HttpServletRequest request,
                                        @NotNull HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> userAttributes = oAuth2User.getAttributes();

        String email = (String) userAttributes.get("email");
        String firstName = (String) userAttributes.get(GOOGLE_OAUTH_1ST_NAME_KEY);
        String lastName = (String) userAttributes.get(GOOGLE_OAUTH_2ND_NAME_KEY);

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setConfirmed(true);
            newUser.setRole(roleRepository.findByRole("ROLE_USER").get());
            return userRepository.save(newUser);
        });

        TokenResponse tokenResponse = tokenService.getTokens(user.getId());

        setRefreshTokenInCookie(tokenResponse.getRefreshToken(), response);

        String redirectUri = UriComponentsBuilder.fromUriString("http://localhost:3000/api/auth/callback")
                .queryParam("token", tokenResponse.getAccessToken())
                .queryParam("expiresIn", tokenResponse.getAccessTokenExpiresIn())
                .build()
                .toUriString();

        response.sendRedirect(redirectUri);

    }

    private void setRefreshTokenInCookie(String rt, HttpServletResponse response) {
        Cookie cookie =  new Cookie("refreshToken", rt);
        cookie.setPath("/");
        cookie.setMaxAge((int)(Long.parseLong(rtExpiry)));
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
