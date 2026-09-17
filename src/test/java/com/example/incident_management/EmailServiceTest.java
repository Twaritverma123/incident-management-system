package com.example.incident_management;


import com.example.incident_management.service.EmailService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    @Disabled
    public void testSendEmail() {

        emailService.sendEmail(
                "twarit2002@gmail.com",
                "Incident Management System - Test Email",
                "Hello!\n\n"
                        + "This is a test email from the Incident Management System.\n\n"
                        + "Email notification feature is working successfully.\n\n"
                        + "Regards,\n"
                        + "Incident Management System"
        );
    }
}