package DUA.pet.com.controller;

import DUA.pet.com.model.PagamentoModel;
import DUA.pet.com.service.PagamentoService;
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
        return pagamentoService.findById(id)
                .map(p -> ResponseEntity.ok(p))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<PagamentoModel>> findAll() {
        List<PagamentoModel> pagamentos = pagamentoService.findAll();
        if (pagamentos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pagamentos);
    }

    /* tira esse comentario quando juntar com o PedidoModel
    @PostMapping
    public ResponseEntity<PagamentoModel> save(@Valid @RequestBody PagamentoModel pagamento) {
        try {
            PagamentoModel salvo = pagamentoService.save(pagamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }
    */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            pagamentoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
