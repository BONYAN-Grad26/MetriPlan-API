package com.abdelaziz26.metriplate.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Schema(description = "User login request")
public class LoginDto {

    @Schema(
            description = "Registered email address",
            example = "mohamed@example.com"
    )
    @Email
    @NotBlank
    private String email;

    @Schema(
            description = "Account password",
            example = "P@ssword123"
    )
    @NotBlank
    private String password;
}