package com.abdelaziz26.metriplate.repositories;

import com.abdelaziz26.metriplate.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    public Optional<RefreshToken> findByToken(String token);
    public List<RefreshToken> findByUserId(Long userId);
}
