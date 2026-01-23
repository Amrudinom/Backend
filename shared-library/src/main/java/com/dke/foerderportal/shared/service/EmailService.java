package com.dke.foerderportal.shared.service;

import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.Method;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final SendGrid sendGrid;

    @Value("${mail.from}")
    private String from;

    public void sendEmail(String to, String subject, String body) {
        try {
            Mail mail = new Mail(
                    new Email(from),
                    subject,
                    new Email(to),
                    new Content("text/plain", body)
            );

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            int status = response.getStatusCode();
            if (status >= 200 && status < 300) {
                log.info("SendGrid: Email sent successfully to: {}", to);
            } else {
                log.warn("SendGrid: status={} body={}", status, response.getBody());
            }
        } catch (Exception e) {
            log.error("SendGrid: Failed to send email to: {}", to, e);
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

    public void sendAntragInBearbeitungEmail(String to, String antragTitel) {
        String subject = "Förderantrag in Bearbeitung";
        String body = String.format(
                "Hallo,\n\n" +
                        "Ihr Förderantrag '%s' wird nun bearbeitet.\n\n" +
                        "Mit freundlichen Grüßen,\n" +
                        "Ihr Förderportal-Team",
                antragTitel
        );
        sendEmail(to, subject, body);
    }
}
