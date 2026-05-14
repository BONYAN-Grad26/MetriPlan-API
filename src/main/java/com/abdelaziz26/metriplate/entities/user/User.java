package com.abdelaziz26.metriplate.entities.user;

import com.abdelaziz26.metriplate.entities.diet.DietPlan;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})
})

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 10)
    @Column(nullable = false)
    private String firstName;

    @Size(min = 3, max = 10)
    @Column(nullable = false)
    private String lastName;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    private String password;

    private String otp;

    private LocalDateTime otpExpiry;

    private boolean confirmed = false;


    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Allergy> allergens = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade =  CascadeType.ALL, orphanRemoval = true)
    private List<Goal> goals;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private HealthMetrics healthMetrics;

    @OneToMany(mappedBy = "user", cascade =  CascadeType.ALL, orphanRemoval = true)
    private List<DietPlan> dietPlans;

    @OneToMany(mappedBy = "user", cascade =  CascadeType.ALL, orphanRemoval = true)
    private List<RefreshToken> refreshTokens;

    @Override
    public @NotNull Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getUsername() {
        return email;
    }
}
