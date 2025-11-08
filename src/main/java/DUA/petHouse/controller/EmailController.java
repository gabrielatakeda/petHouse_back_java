package DUA.petHouse.controller;

import DUA.petHouse.model.Email;
import DUA.petHouse.model.EmailRecuperacao;
import DUA.petHouse.model.UsuarioModel;
import DUA.petHouse.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-mail")
    public ResponseEntity<Void> enviarEmail(@RequestBody EmailRecuperacao request) {
        try {
            Email email = new Email(
                    request.getEmail(),
                    "Recuperação de senha",
                    "Aqui está o seu link para redefinir a senha..."
            );

            emailService.enviarEmailSimples(email);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


