package DUA.petHouse.controller;

import DUA.petHouse.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send-mail")
    public String enviarEmail() {
        emailService.enviarEmailSimples(
                "destinatario@gmail.com",
                "Teste de envio com Gmail",
                "Olá! Este é um e-mail enviado via Spring Boot + Gmail."
        );
        return "E-mail enviado com sucesso!";
    }

}
