package DUA.pet.com.service;

import DUA.pet.com.model.PedidoModel;
import DUA.pet.com.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
