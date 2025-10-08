package com.example.iot.controller;

import com.example.iot.model.PedidoModel;
import com.example.iot.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping("/save")
    public ResponseEntity<PedidoModel> save(@RequestBody @Valid PedidoModel pedidoModel) {
        try {
            var result = pedidoService.save(pedidoModel);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<PedidoModel>> findAll(){
        try{
            var result = pedidoService.findAll();
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<PedidoModel> findById(@PathVariable Long id){
        try{
            var result = pedidoService.findById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}

