package DUA.pet.petHouse.controller;

import DUA.pet.petHouse.model.UsuarioModel;
import DUA.pet.petHouse.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
//@CrossOrigin("*")
@CrossOrigin(origins = "http://localhost:4200")

public class UsuarioController {

    @Autowired
    private final UsuarioService usuarioService;

    @PostMapping("/save")
    public ResponseEntity<UsuarioModel> save(@RequestBody @Valid UsuarioModel usuarioModel) {
        try {
            var result = usuarioService.save(usuarioModel);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<UsuarioModel> findById(@PathVariable Long id){
        try{
            var result = usuarioService.findById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
        public ResponseEntity<Void> delete(@PathVariable Long id){
        try{
            usuarioService.delete(id);
            return ResponseEntity.noContent().build();
        }catch (Exception ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UsuarioModel> update(@PathVariable Long id, @RequestBody @Valid UsuarioModel usuarioUpdate){
        try{
            var result = usuarioService.update(id, usuarioUpdate);
            return new ResponseEntity(result,HttpStatus.OK);
        } catch (Exception ex) {
            return  new ResponseEntity(null,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<UsuarioModel>> findAll(){
        try{
            var result = usuarioService.findAll();
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String usuarioLogin = credentials.get("usuarioLogin");
        String senha = credentials.get("senha");

        try {
            String token = usuarioService.login(usuarioLogin, senha);

            return ResponseEntity.ok(Map.of("token", token));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }


}
