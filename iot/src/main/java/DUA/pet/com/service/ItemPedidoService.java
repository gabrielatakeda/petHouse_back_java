package DUA.pet.com.service;

import DUA.pet.com.model.ItemPedido;
import DUA.pet.com.repository.ItemPedidoRepository;
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
