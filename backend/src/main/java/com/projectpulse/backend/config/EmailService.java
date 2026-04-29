package com.projectpulse.backend.config;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.*;
import com.sendgrid.helpers.mail.objects.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    public void sendInviteEmail(String toEmail, String toName, String tempPassword) throws Exception {
        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        String subject = "You've been invited to Project Pulse";
        Content content = new Content("text/html",
            "<h2>Welcome to Project Pulse, " + toName + "!</h2>" +
            "<p>You have been invited to join Project Pulse.</p>" +
            "<p>Your temporary password is: <strong>" + tempPassword + "</strong></p>" +
            "<p>Please log in at: <a href='https://brave-flower-0bf6c5f0f.7.azurestaticapps.net'>Project Pulse</a></p>" +
            "<p>We recommend changing your password after logging in.</p>"
        );
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        sg.api(request);
    }
}
