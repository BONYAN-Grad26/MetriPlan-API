package com.abdelaziz26.metriplate.services.email;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendOtpEmail(
            String to,
            String name,
            String otp,
            int expiryMinutes
    ) throws MessagingException;
}
