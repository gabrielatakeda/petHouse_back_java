package DUA.pet.petHouse.service;

import DUA.pet.petHouse.enums.MetodoPagamento;
import DUA.pet.petHouse.enums.StatusPagamento;
import DUA.pet.petHouse.model.PagamentoModel;
import DUA.pet.petHouse.model.PedidoModel;
import DUA.pet.petHouse.repository.PagamentoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {

    @InjectMocks
    PagamentoService service;

    @Mock
    PagamentoRepository repository;

    PagamentoModel pagamento;

    @BeforeEach
    void setUp() {
        PedidoModel pedido = new PedidoModel();
        pedido.setId(1L);

        pagamento = new PagamentoModel();
        pagamento.setId(1L);
        pagamento.setPedido(pedido);
        pagamento.setMetodoPagamento(MetodoPagamento.CARTAO);
        pagamento.setStatusPagamento(StatusPagamento.PENDENTE);
        pagamento.setDataPagamento(LocalDate.now());
    }

    @Test
    @DisplayName("Teste função findAll PagamentoService")
    void findAllTest () {
        when(repository.findAll()).thenReturn(List.of(pagamento));

        var response = service.findAll();
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.size() > 0);
    }

    @Test
    @DisplayName("Teste função delete PagamentoService")
    void deleteByIdTest () {
        doNothing().when(repository).deleteById(1L);

        service.deleteById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Teste função findById PagamentoService")
    void findByIdTest () {
        when(repository.findById(1L)).thenReturn(Optional.of(pagamento));

        var response = service.findById(1L);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.get().getId() , pagamento.getId());
    }

}
