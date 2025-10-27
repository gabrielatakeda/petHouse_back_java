package DUA.petHouse.service;

import jakarta.mail.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = spy(new EmailService());
        TestUtils.setField(emailService, "usuario", "teste@exemplo.com");
        TestUtils.setField(emailService, "senha", "123456");
    }

    @Test
    @DisplayName("Deve enviar e-mail com sucesso quando dados válidos forem fornecidos")
    void enviarEmailComSucessoTest() {
        try (MockedStatic<Transport> transportMock = mockStatic(Transport.class)) {

            assertDoesNotThrow(() -> emailService.enviarEmail(
                    "destinatario@teste.com", "Assunto Teste", "Corpo do e-mail"));

            verify(emailService, times(1)).criarSessao();
            transportMock.verify(() -> Transport.send(any(Message.class)), times(1));
        }
    }

    @Test
    @DisplayName("Deve substituir corpo nulo por string vazia")
    void enviarEmailCorpoNuloTest() {
        try (MockedStatic<Transport> transportMock = mockStatic(Transport.class)) {

            assertDoesNotThrow(() -> emailService.enviarEmail(
                    "destinatario@teste.com", "Assunto Teste", null));

            transportMock.verify(() -> Transport.send(any(Message.class)), times(1));
        }
    }

    @Test
    @DisplayName("Deve capturar exceção MessagingException e não lançar")
    void enviarEmailLancaExcecaoTest() throws Exception {
        try (MockedStatic<Transport> transportMock = mockStatic(Transport.class)) {
            transportMock.when(() -> Transport.send(any(Message.class)))
                    .thenThrow(new MessagingException("Falha SMTP"));

            assertDoesNotThrow(() -> emailService.enviarEmail(
                    "destinatario@teste.com", "Assunto Teste", "Corpo"));

            transportMock.verify(() -> Transport.send(any(Message.class)), times(1));
        }
    }

    @Test
    @DisplayName("Deve criar sessão de e-mail com propriedades corretas")
    void criarSessaoTest() {
        Session sessao = emailService.criarSessao();
        Properties props = sessao.getProperties();

        assertEquals("true", props.getProperty("mail.smtp.auth"));
        assertEquals("true", props.getProperty("mail.smtp.starttls.enable"));
        assertEquals("smtp.gmail.com", props.getProperty("mail.smtp.host"));
        assertEquals("587", props.getProperty("mail.smtp.port"));
    }

    // Classe auxiliar para injetar valores privados
    static class TestUtils {
        static void setField(Object target, String fieldName, Object value) {
            try {
                var field = target.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
