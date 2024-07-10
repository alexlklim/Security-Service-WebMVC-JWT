package com.alex.security.email;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final String TAG = "EMAIL_SERVICE - ";
    private final JavaMailSender mailSender;

    public void createMail(EmailStructure emailStructure, String body) {
        log.info(TAG + "createMail");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailStructure.getEmail());
        simpleMailMessage.setSubject(emailStructure.getEmailType().getSubject());
        simpleMailMessage.setText(body);
        mailSender.send(simpleMailMessage);
    }


    public String createBody(EmailType emailType) {
        log.info(TAG + "createBody");
        return switch (emailType) {
            case ANNOUNCEMENT ->
                    "We have an exciting announcement for you! " +
                            "Stay tuned for more details.";
            case PASSWORD_WAS_CHANGED ->
                    "Your password was successfully changed. " +
                            "If you did not perform this action, please contact support immediately.";
            default -> "No body text available for this email type.";
        };
    }

    public String createBody(EmailType emailType, String link){
        log.info(TAG + "createBody");
        return switch (emailType) {
            case CONFIRM_EMAIL ->
                    "Thank you for signing up! " +
                            "\nPlease confirm your email by clicking on the link below." +
                            "\n Click: " + link;
            case FORGOT_PASSWORD ->
                    "It seems you have forgotten your password. " +
                            "\nPlease use the link below to reset it." +
                            "\n Click: " + link;
            default -> "No body text available for this email type.";
        };
    }

}
