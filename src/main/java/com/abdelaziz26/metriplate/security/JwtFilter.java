package com.abdelaziz26.metriplate.security;

import com.abdelaziz26.metriplate.entities.user.User;
import com.abdelaziz26.metriplate.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        boolean white = request.getServletPath().startsWith("/api/auth")
                || request.getServletPath().startsWith("/v3/api-docs")
                || request.getServletPath().startsWith("/swagger-ui")
                || request.getServletPath().startsWith("/swagger-ui.html")
                || (request.getMethod().equals("GET") && request.getServletPath().startsWith("/api/ingredients"))
                || (request.getMethod().equals("GET") && request.getServletPath().startsWith("/api/tags"));

        if (white) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Request URI is {}", request.getRequestURI());

        String authHeader = request.getHeader("Authorization");

        if(authHeader == null ) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader;

        if(authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if(token.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtService.extractUserName(token);

        //log.info("Email is {}", email);

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            Optional<User> user = userRepository.findByEmail(email);

            if(user.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            boolean isValid = jwtService.validateToken(token, user.get());

            if(!isValid) {
                filterChain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user.get(), null, user.get().getAuthorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);

        }

        filterChain.doFilter(request, response);
    }

}
