package com.example.backend.spring.security;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

public class MailVerification {

    /**
     *  Sending of email verification for newly registered user
     *  Only run when the user is successfully registered
     * */
    public void verificationRequest(
            String sender, String username, String destination, String token, JavaMailSender mailSender) throws MessagingException, UnsupportedEncodingException {

        // Email body text
        String content = "Dear " + username + ",<br>"
                + "Click the link to verify your registration:<br>"
                + "<h3><a href=\"http://localhost:8080/verify?token=" + token + "\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you <3<br>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(sender, "Temps Monitor");
        helper.setTo(destination);
        helper.setSubject("Confirmation Link");
        helper.setText(content, true);

        mailSender.send(message);
    }

}
