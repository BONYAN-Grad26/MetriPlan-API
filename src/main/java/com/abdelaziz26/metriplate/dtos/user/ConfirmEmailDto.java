package com.abdelaziz26.metriplate.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Email confirmation request")
public class ConfirmEmailDto {

    @Schema(
            description = "Registered email address",
            example = "mohamed@example.com"
    )
    @Email
    private String email;

    @Schema(
            description = "6-digit OTP code",
            example = "123456"
    )
    private String otp;
}
