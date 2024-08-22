package com.qkforest.userservice.service;

import com.qkforest.commonmodule.exception.BusinessLogicException;
import com.qkforest.commonmodule.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String title, String text) {
        SimpleMailMessage email = createEmail(to, title, text);
        try {
            javaMailSender.send(email);
        } catch (RuntimeException e) {
            log.debug("MailService.sendEmail exception occur to: {}, " +
                    "title: {}, text: {}", to, title, text);
            throw new BusinessLogicException(ExceptionCode.UNABLE_TO_SEND_EMAIL);
        }
    }

    private SimpleMailMessage createEmail(String to, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(title);
        message.setText(text);

        return message;
    }
}