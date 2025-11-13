package com.Foerderportal.Backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@foerderportal.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to: {}", to, e);
            // Don't throw exception - email failure shouldn't break the application
        }
    }

    public void sendAntragEingereichtEmail(String to, String antragTitel) {
        String subject = "Förderantrag erfolgreich eingereicht";
        String body = String.format(
                "Hallo,\n\n" +
                        "Ihr Förderantrag '%s' wurde erfolgreich eingereicht.\n\n" +
                        "Sie erhalten eine Benachrichtigung, sobald Ihr Antrag bearbeitet wurde.\n\n" +
                        "Mit freundlichen Grüßen,\n" +
                        "Ihr Förderportal-Team",
                antragTitel
        );
        sendEmail(to, subject, body);
    }

    public void sendAntragGenehmigtEmail(String to, String antragTitel) {
        String subject = "Förderantrag genehmigt";
        String body = String.format(
                "Hallo,\n\n" +
                        "Gute Nachrichten! Ihr Förderantrag '%s' wurde genehmigt.\n\n" +
                        "Mit freundlichen Grüßen,\n" +
                        "Ihr Förderportal-Team",
                antragTitel
        );
        sendEmail(to, subject, body);
    }

    public void sendAntragAbgelehntEmail(String to, String antragTitel, String grund) {
        String subject = "Förderantrag abgelehnt";
        String body = String.format(
                "Hallo,\n\n" +
                        "Ihr Förderantrag '%s' wurde leider abgelehnt.\n\n" +
                        "Grund: %s\n\n" +
                        "Mit freundlichen Grüßen,\n" +
                        "Ihr Förderportal-Team",
                antragTitel,
                grund
        );
        sendEmail(to, subject, body);
    }

    public void sendWelcomeEmail(String to, String name) {
        String subject = "Willkommen beim Förderportal";
        String body = String.format(
                "Hallo %s,\n\n" +
                        "Willkommen beim Förderportal!\n\n" +
                        "Sie können sich jetzt einloggen und Förderanträge stellen.\n\n" +
                        "Mit freundlichen Grüßen,\n" +
                        "Ihr Förderportal-Team",
                name
        );
        sendEmail(to, subject, body);
    }
}