package com.example.iot.auth;

import org.springframework.beans.factory.annotation.Autowired; //Import para injeção de dependência
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController //Recebe requisições HTTP, processa e devolve para o front
@RequestMapping("/api/login") //Prefixo da URL
@CrossOrigin("*") //Permite que qualquer origem (qualquer porta) faça requisições à API
public class LoginController {

    @Autowired //Não precisa digitar new LoginService(), pois essa anotação faz isso automaticamente
    private LoginService loginService;

    @PostMapping //Requisição POST
    public ResponseEntity<String> logar(@RequestBody Login login){ //Recebe um objeto login vindo pelo RequestBody e retorna uma resposta HTTP
        try{
            return ResponseEntity.ok(loginService.logar(login)); //Retorna token e status 200
        }catch (AuthenticationException ex){ //Se o login falhar
            System.out.println(ex.getMessage());
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED); //Retorna status 401
        } catch (Exception e) { //Qualquer erro inesperado
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); //Retorna status 400
        }
    }
}
