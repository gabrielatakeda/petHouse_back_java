package DUA.petHouse.service;

import DUA.petHouse.model.Email;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmailSimples(Email email) {
        var message = new SimpleMailMessage();
        message.setFrom("pethouse@gmail.com");
        message.setTo(email.para());
        message.setSubject(email.assunto());
        message.setText(email.texto());
        mailSender.send(message);
    }
}
