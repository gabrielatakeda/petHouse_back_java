package DUA.pet.petHouse.service;

import com.example.petHouse.model.ItemPedido;
import DUA.pet.petHouse.repository.ItemPedidoRepository;
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
