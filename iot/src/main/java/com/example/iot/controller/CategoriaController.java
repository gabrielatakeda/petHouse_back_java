package com.example.iot.controller;

import com.example.iot.model.CategoriaModel;
import com.example.iot.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController  // DEFINE A CLASSE COMO UM CONTROLADOR REST
@RequestMapping("/api/ecommerce")  // DEFINE ENDPOINT BASE DA API
@RequiredArgsConstructor  // GERA CONSTRUTOR AUTOMÁTICO PARA OS ATRIBUTOS FINAL (LOMBOK)
public class CategoriaController {


    // FINAL GARANTE QUE A VARIÁVEL NÃO SERÁ REATRIBUÍDA
    private final CategoriaService categoriaService;

    @GetMapping("/findAll")
    public ResponseEntity<List<CategoriaModel>> findAll() {
        try{
            var result = categoriaService.findAll();
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception ex){
            return  new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<CategoriaModel> findById(@PathVariable Long id) {
        try {
            var result = categoriaService.findById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/nome")
    public ResponseEntity<Optional<CategoriaModel>> findByNome(@RequestParam String nome) {
        try {
            var result = categoriaService.findByNome(nome);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/save")
    public ResponseEntity<CategoriaModel> save(@RequestBody @Valid CategoriaModel categoria) {
        try {
            var result = categoriaService.save(categoria);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        }
    }

    // PUT - ATUALIZAR
    @PutMapping("/update/{id}")
    public ResponseEntity<CategoriaModel> update(@PathVariable Long id, @RequestBody CategoriaModel categoria){
        try{
            var result = categoriaService.update(id,categoria);
            return new ResponseEntity(result,HttpStatus.OK);
        } catch (Exception ex) {
            return  new ResponseEntity(null,HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            categoriaService.deleteById(id);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        }
    }


}
