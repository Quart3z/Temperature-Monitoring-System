package com.example.backend.spring.security;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

public class MailVerification {

    //     PLEASE CHANGE THIS BEFORE PUBLISHED
    public void verificationRequest(String username, String destination, String token, JavaMailSender mailSender) throws MessagingException, UnsupportedEncodingException {

        String content = "Dear " + username + ",<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"http://localhost:8080/verify?token=" + token + "\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("looiyaowei@gmail.com", "Me and myself");
        helper.setTo(destination);
        helper.setSubject("Confirmation Link");
        helper.setText(content, true);

        mailSender.send(message);
    }

}
