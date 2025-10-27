package DUA.petHouse.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    @Value("${mail.username}")
    private String usuario;

    @Value("${mail.password}")
    private String senha;

    // Método protegido para facilitar teste
    protected Session criarSessao() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, senha);
            }
        });
    }

    public void enviarEmail(String destinatario, String assunto, String corpo) {
        try {
            if (corpo == null) corpo = ""; // evita NullPointer
            Message message = new MimeMessage(criarSessao());
            message.setFrom(new InternetAddress(usuario));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(assunto);
            message.setText(corpo);

            Transport.send(message);

            System.out.println("E-mail enviado com sucesso para " + destinatario);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
        }
    }
}
