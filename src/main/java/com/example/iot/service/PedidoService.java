package com.example.iot.service;

import com.example.iot.model.PedidoModel;
import com.example.iot.repository.PedidoRepository;
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
