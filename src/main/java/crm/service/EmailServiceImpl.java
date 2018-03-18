package src.main.java.crm;

import src.main.java.crm.exceptions.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;


@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender javaMailSenderImpl;



    public void sendWithoutAttachment(String to, String subject, String text) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);

        javaMailSenderImpl.send(simpleMailMessage);

    }


    public void sendWithAttachmentInside(String to, String subject, String text, boolean isHtmlText) {


        MimeMessage mimeMessage = javaMailSenderImpl.createMimeMessage();

        try {
            // use the true flag to indicate you need a multipart message
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, isHtmlText);
        } catch (MessagingException exc) {
            throw new BadRequestException("BAD_REQUEST");
        }

        javaMailSenderImpl.send(mimeMessage);
    }

}