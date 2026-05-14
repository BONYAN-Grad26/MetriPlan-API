package com.abdelaziz26.metriplate.entities.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name = "resfresh_tokens")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class RefreshToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private boolean revoked;

    private LocalDateTime expiresAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

}
