package org.telemedicine.server.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;
    public void sendVerificationMail(String email, String content) throws MessagingException {
        String subject = "Xác thực email của bạn";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper  helper = new MimeMessageHelper(message, true);
        helper.setFrom("${spring.mail.username}");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }
}
