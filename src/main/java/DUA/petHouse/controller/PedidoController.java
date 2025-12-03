package DUA.petHouse.controller;

import DUA.petHouse.model.PedidoModel;
import DUA.petHouse.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping("/save")
    public ResponseEntity<PedidoModel> save(@RequestBody @Valid PedidoModel pedidoModel) {
        try {
            log.info("Recebendo pedido para salvar: {}", pedidoModel);
            var result = pedidoService.save(pedidoModel);
            log.info("Pedido salvo com sucesso: {}", result);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Erro ao salvar pedido", ex);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<PedidoModel>> findAll(){
        try{
            log.info("Buscando todos os pedidos");
            var result = pedidoService.findAll();
            log.info("Total de pedidos encontrados: {}", result.size());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception ex){
            log.error("Erro ao buscar todos os pedidos", ex);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<PedidoModel> findById(@PathVariable Long id){
        try{
            log.info("Buscando pedido pelo id: {}", id);
            var result = pedidoService.findById(id);
            log.info("Pedido encontrado: {}", result);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception ex){
            log.error("Erro ao buscar pedido por id", ex);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findByCliente/{clienteId}")
    public ResponseEntity<Optional<PedidoModel>> findByCliente(@PathVariable Long clienteId) {
        try {
            log.info("Buscando pedido pelo clienteId: {}", clienteId);
            var result = pedidoService.findByCliente(clienteId);
            log.info("Pedido encontrado para cliente {}: {}", clienteId, result);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Erro ao buscar pedido por clienteId", ex);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // NOVO ENDPOINT: remover produto de um pedido
    @DeleteMapping("/{pedidoId}/produto/{produtoId}")
    public ResponseEntity<PedidoModel> removerProduto(
            @PathVariable Long pedidoId,
            @PathVariable Long produtoId) {
        try {
            log.info("Removendo produto {} do pedido {}", produtoId, pedidoId);
            var pedidoAtualizado = pedidoService.removerProduto(pedidoId, produtoId);
            log.info("Produto removido com sucesso. Pedido atualizado: {}", pedidoAtualizado);
            return new ResponseEntity<>(pedidoAtualizado, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Erro ao remover produto do pedido", ex);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
