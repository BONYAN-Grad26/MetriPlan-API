package com.abdelaziz26.metriplate.configurations;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class AppConfig {

    @Value("${SMTP_HOST}")
    private  String smtpHost;

    @Value("${SMTP_PORT}")
    private int smtpPort;

    @Value("${SMTP_USERNAME}")
    private String smtpUsername;

    @Value("${SMTP_PASSWORD}")
    private String smtpPassword;



    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary();
    }

    @Bean
    public JavaMailSender mailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpHost);
        mailSender.setPort(smtpPort);
        mailSender.setUsername(smtpUsername);
        mailSender.setPassword(smtpPassword);

        Properties props = mailSender.getJavaMailProperties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.transport.protocol", "smtp");

        return mailSender;
    }
}
