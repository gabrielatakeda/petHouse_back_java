package com.example.iot.controller;

import com.example.iot.model.EnderecoModel;
import com.example.iot.service.EnderecoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EnderecoController {

    @Autowired
    private final EnderecoService enderecoService;

    @PostMapping("/save")
    public ResponseEntity<EnderecoModel> save(@RequestBody @Valid EnderecoModel enderecoModel) {
        try {
            var result = enderecoService.save(enderecoModel);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<EnderecoModel> findById(@PathVariable Long id){
        try{
            var result = enderecoService.findById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        try{
            enderecoService.delete(id);
            return ResponseEntity.noContent().build();
        }catch (Exception ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EnderecoModel> update(@PathVariable Long id, @RequestBody @Valid EnderecoModel enderecoUpdate){
        try{
            var result = enderecoService.update(id, enderecoUpdate);
            return new ResponseEntity(result,HttpStatus.OK);
        } catch (Exception ex) {
            return  new ResponseEntity(null,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<EnderecoModel>> findAll(){
        try{
            var result = enderecoService.findAll();
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
