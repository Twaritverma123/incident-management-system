package com.example.incident_management.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String mailUsername;

    public void sendEmail(String to, String subject, String text) {

        try {

            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(mailUsername);
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(text);

            mailSender.send(mail);

            log.info("Email sent successfully to {}", to);

        } catch (Exception e) {

            e.printStackTrace();
            throw e;
        }
    }

}
