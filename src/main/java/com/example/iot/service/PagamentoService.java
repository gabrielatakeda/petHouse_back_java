package com.example.iot.service;

import com.example.iot.model.PagamentoModel;
import com.example.iot.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    /* tira esse comentario quando juntar com o PedidoModel
    public PagamentoModel save(PagamentoModel pagamento) {
        if (pagamento.getPedido() == null) {
            throw new IllegalArgumentException("O pagamento precisa estar associado a um pedido");
        }
        return pagamentoRepository.save(pagamento);
    }
    */

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
