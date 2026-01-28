package com.abdelaziz26.metriplate.services.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value(("${SMTP_USERNAME}"))
    private String fromEmail;

    public void sendOtpEmail(
            String to,
            String name,
            String otp,
            int expiryMinutes
    ) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, true, "UTF-8");

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("otp", otp);
        context.setVariable("expiryMinutes", expiryMinutes);

        String html =
                templateEngine.process("otp-email", context);

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject("MetriPlate - OTP Verification");
        helper.setText(html, true);

        mailSender.send(message);
    }

}
