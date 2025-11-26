package DUA.petHouse.config;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    //Erros de validations
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handle01(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();
        for (FieldError fildError : ex.getBindingResult().getFieldErrors()) {
            erros.put(fildError.getField(), fildError.getDefaultMessage());
        }
        return new ResponseEntity<Map<String, String>>(erros, HttpStatus.BAD_REQUEST);
    }

    //Erro de validação
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handle02(ConstraintViolationException ex) {
        Map<String, String> erros = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            erros.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return new ResponseEntity<Map<String, String>>(erros, HttpStatus.BAD_REQUEST);
    }

    //Tratamento de outros erros da aplicação e regras de negócio
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle03(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
