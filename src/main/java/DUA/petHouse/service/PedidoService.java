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

}
