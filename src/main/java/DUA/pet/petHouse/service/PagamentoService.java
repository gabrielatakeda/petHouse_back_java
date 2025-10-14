package DUA.pet.petHouse.service;

import DUA.pet.petHouse.model.PagamentoModel;
import DUA.pet.petHouse.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;


    public List<PagamentoModel> findAll() {
        return pagamentoRepository.findAll();
    }

    public Optional<PagamentoModel> findById(Long id) {
        return pagamentoRepository.findById(id);
    }

    public void deleteById(Long id) {
        pagamentoRepository.deleteById(id);
    }
}
