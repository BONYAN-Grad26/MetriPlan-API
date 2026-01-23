package com.abdelaziz26.metriplate.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtService {

    @Value("${JWT_SECRET_KEY}")
    private String JWT_SECRET_KEY;

    @Value("${JWT_EXPIRY}")
    private Long JWT_EXPIRY;

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, new HashMap<>());
    }

    public String generateToken(UserDetails userDetails, Map<String, Object> claims) {

        claims.put("authorities",
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRY))
                .signWith(getSecretKey())
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails)
    {
        return !isExpired(token) && extractUserName(token).equals(userDetails.getUsername());
    }

    public boolean isExpired(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
