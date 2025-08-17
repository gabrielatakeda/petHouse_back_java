package com.example.iot.controller;

import com.example.iot.model.UsuarioModel;
import com.example.iot.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    @Autowired
    private final UsuarioService usuarioService;

    @PostMapping("/save")
    public ResponseEntity<UsuarioModel> save(@RequestBody UsuarioModel usuarioModel) {
        try {
            var result = usuarioService.save(usuarioModel);
            return new ResponseEntity<>(result, HttpStatus.OK);
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

}
