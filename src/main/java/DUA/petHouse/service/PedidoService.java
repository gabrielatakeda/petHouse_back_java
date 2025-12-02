package DUA.petHouse.service;

import DUA.petHouse.model.PedidoModel;
import DUA.petHouse.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoModel save(PedidoModel pedidoModel){
        return pedidoRepository.save(pedidoModel);
    }

    public List<PedidoModel> findAll(){
        return pedidoRepository.findAll();
    }

    public PedidoModel findById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    public Optional<PedidoModel> findByCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    // NOVO MÉTODO: remover produto de um pedido
    public PedidoModel removerProduto(Long pedidoId, Long produtoId) {
        PedidoModel pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        // remove o produto da lista pelo id
        pedido.getProdutos().removeIf(p -> p.getId().equals(produtoId));

        // recalcula o total
        double novoTotal = pedido.getProdutos().stream()
                .mapToDouble(p -> (p.getPrecoVenda() != null ? p.getPrecoVenda() : 0.0) *
                        (p.getQuantidadeSelecionada() != null ? p.getQuantidadeSelecionada() : 1))
                .sum();
        pedido.setTotal(novoTotal);

        return pedidoRepository.save(pedido);
    }


}
