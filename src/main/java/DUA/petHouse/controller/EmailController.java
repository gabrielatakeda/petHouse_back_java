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
                "destinatario@gmail.com", //tenho que pensar como pegar o email do cara para colocar aqui
                "Teste",
                "teste"
        );
        return "E-mail enviado com sucesso!";
    }

}
