package com.abdelaziz26.metriplate.entities.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

@Entity @Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String role;

    @Override
    public @Nullable String getAuthority() {
        return role;
    }

}
