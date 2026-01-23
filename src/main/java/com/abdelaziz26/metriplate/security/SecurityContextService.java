package com.abdelaziz26.metriplate.security;

import com.abdelaziz26.metriplate.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityContextService {

    public Optional<User> getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }

        if(authentication.getPrincipal() instanceof User) {
            return Optional.of((User) authentication.getPrincipal());
        }
        return Optional.empty();
    }
}
