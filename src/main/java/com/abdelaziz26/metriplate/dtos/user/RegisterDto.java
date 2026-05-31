package com.abdelaziz26.metriplate.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Setter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "User registration request")
public class RegisterDto {

    @Schema(
            description = "User first name",
            example = "Mohamed"
    )
    @Size(min = 1, max = 100)
    private String firstName;

    @Schema(
            description = "User last name",
            example = "Abdelaziz"
    )
    @Size(min = 1, max = 100)
    private String lastName;

    @Schema(
            description = "Email address",
            example = "mohamed@example.com"
    )
    @Email
    private String email;

    @Schema(
            description = "User password",
            example = "P@ssword123"
    )
    @NotBlank
    private String password;
}
