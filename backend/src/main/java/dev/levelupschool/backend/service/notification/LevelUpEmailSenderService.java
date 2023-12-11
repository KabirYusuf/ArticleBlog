package dev.levelupschool.backend.service.notification;

import dev.levelupschool.backend.data.dto.request.NotificationRequest;
import dev.levelupschool.backend.exception.ConfigurationException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class LevelUpEmailSenderService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Async
    public void sendEmailNotification(NotificationRequest notificationRequest){
        String emailContent = springTemplateEngine.process(notificationRequest.getContent(), notificationRequest.getContext());
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
        try {
            mimeMessageHelper.setSubject(notificationRequest.getSubject());
            mimeMessageHelper.setTo(notificationRequest.getTo());
            mimeMessageHelper.setFrom("kabir_levelup@gmail.com");
            mimeMessageHelper.setText(emailContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new ConfigurationException(e.getMessage());
        }
    }
}
