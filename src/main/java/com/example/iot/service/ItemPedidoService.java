package com.example.iot.service;

import com.example.iot.model.ItemPedido;
import com.example.iot.repository.ItemPedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemPedidoService {

    private final ItemPedidoRepository itemPedidoRepository;

    public ItemPedido save(ItemPedido itemPedido){
        return itemPedidoRepository.save(itemPedido);
    }

}
