package DUA.pet.petHouse.service;

import DUA.pet.petHouse.enums.StatusPedido;
import DUA.pet.petHouse.model.PedidoModel;
import DUA.pet.petHouse.model.UsuarioModel;
import DUA.pet.petHouse.repository.PedidoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @InjectMocks
    PedidoService service;

    @Mock
    PedidoRepository repository;

    PedidoModel pedido;

    @BeforeEach
    void setUp() {
        UsuarioModel cliente = new UsuarioModel();
        cliente.setId(1L);
        cliente.setNome("João Luiz Martinazzo");

        pedido = new PedidoModel();
        pedido.setId(1L);
        pedido.setCliente(cliente);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setTotal(200.50);
        pedido.setStatusPedido(StatusPedido.PENDENTE);
    }

    @Test
    @DisplayName("Teste função findAll PedidoService")
    void findAllTest() {
        when(repository.findAll()).thenReturn(List.of(pedido));

        var response = service.findAll();

        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isEmpty());
        Assertions.assertEquals(pedido.getId(), response.get(0).getId());
    }

    @Test
    @DisplayName("Teste função findById PedidoService")
    void findByIdTest() {
        when(repository.findById(1L)).thenReturn(Optional.of(pedido));

        var response = service.findById(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(pedido.getId(), response.getId());
        Assertions.assertEquals(pedido.getTotal(), response.getTotal());
    }

    @Test
    @DisplayName("Teste função save PedidoService")
    void saveTest() {
        when(repository.save(any(PedidoModel.class))).thenReturn(pedido);

        PedidoModel novoPedido = new PedidoModel();
        novoPedido.setId(2L);
        novoPedido.setCliente(pedido.getCliente());
        novoPedido.setDataPedido(LocalDateTime.now());
        novoPedido.setTotal(150.0);
        novoPedido.setStatusPedido(StatusPedido.APROVADO);

        var response = service.save(novoPedido);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(pedido.getId(), response.getId());
        verify(repository, times(1)).save(any(PedidoModel.class));
    }
}
