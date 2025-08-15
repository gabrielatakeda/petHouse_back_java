package com.example.iot.controller;

import com.example.iot.model.PagamentoModel;
import com.example.iot.service.PagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoModel> findById(@PathVariable Long id) {
        try {
            var opt = pagamentoService.findById(id);
            return opt
                    .map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<PagamentoModel>> findAll() {
        try {
            return new ResponseEntity<>(pagamentoService.findAll(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /* tira esse comentario quando juntar com o PedidoModel
    @PostMapping
    public ResponseEntity<PagamentoModel> save(@RequestBody PagamentoModel pagamento) {
        try {
            return new ResponseEntity<>(pagamentoService.save(pagamento), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            pagamentoService.deleteById(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
