package DUA.pet.petHouse.controller;

import DUA.pet.petHouse.enums.MetodoPagamento;
import DUA.pet.petHouse.enums.StatusPagamento;
import DUA.pet.petHouse.enums.StatusPedido;
import DUA.pet.petHouse.model.EnderecoModel;
import DUA.pet.petHouse.model.PagamentoModel;
import DUA.pet.petHouse.model.PedidoModel;
import DUA.pet.petHouse.model.UsuarioModel;
import DUA.pet.petHouse.repository.EnderecoRepository;
import DUA.pet.petHouse.repository.PagamentoRepository;
import DUA.pet.petHouse.repository.PedidoRepository;
import DUA.pet.petHouse.repository.UsuarioRepository;
import DUA.pet.petHouse.service.PagamentoService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PagamentoControllerTest {

    @Mock
    PagamentoService pagamentoService;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    EnderecoRepository enderecoRepository;

    PagamentoModel pagamento;

    @BeforeEach
    void setUp() {
        pagamentoRepository.deleteAll();
        pedidoRepository.deleteAll();

        // Criar e salvar usuário
        UsuarioModel usuario = new UsuarioModel();
        usuario.setNome("Maria");
        usuario.setEmail("maria@email.com");
        usuario.setSenha("Abcd@1234");
        usuario.setUser("mariaUser");
        usuario.setCpf("98765432100");
        usuario.setDataNascimento(LocalDate.of(1995, 5, 5));
        usuario = usuarioRepository.save(usuario);

        //Criar endereço e salvar
        EnderecoModel endereco = new EnderecoModel();
        endereco.setLogradouro("Rua A");
        endereco.setNumero(10);
        endereco.setBairro("Centro");
        endereco.setCidade("Cascavel");
        endereco.setEstado("PR");
        endereco.setCep("85800000");
        endereco.setUsuario(usuario);
        endereco = enderecoRepository.save(endereco);

        // Criar pedido e salvar
        PedidoModel pedido = new PedidoModel();
        pedido.setCliente(usuario);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setTotal(200.0);
        pedido.setStatusPedido(StatusPedido.PENDENTE);
        pedido.setProdutos(new HashSet<>());
        pedido.setEndereco(endereco);
        pedido = pedidoRepository.save(pedido);

        // Criar pagamento com pedido salvo
        pagamento = new PagamentoModel();
        pagamento.setPedido(pedido);
        pagamento.setMetodoPagamento(MetodoPagamento.CARTAO);
        pagamento.setStatusPagamento(StatusPagamento.PENDENTE);
        pagamento.setDataPagamento(LocalDate.now());

        pagamento = pagamentoRepository.save(pagamento);
    }


    @Test
    @DisplayName("findAll Pagamentos")
    void findAllTest() {
        ResponseEntity<PagamentoModel[]> response =
                restTemplate.getForEntity("/pagamentos", PagamentoModel[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("findById existente")
    void findByIdTest() {
        ResponseEntity<PagamentoModel> response =
                restTemplate.getForEntity("/pagamentos/" + pagamento.getId(), PagamentoModel.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("findById inexistente (404)")
    void findByIdNotFound() {
        ResponseEntity<PagamentoModel> response =
                restTemplate.getForEntity("/pagamentos/9999", PagamentoModel.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("deleteById existente")
    void deleteByIdTest() {
        ResponseEntity<Void> response =
                restTemplate.exchange("/pagamentos/" + pagamento.getId(), HttpMethod.DELETE, null, Void.class);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("deleteById inexistente (400)")
    void deleteByIdNotFound() {
        ResponseEntity<Void> response =
                restTemplate.exchange("/pagamentos/9999", HttpMethod.DELETE, null, Void.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("findAll - lista vazia retorna 204 No Content")
    void findAllEmpty() {
        PagamentoController controller = new PagamentoController(pagamentoService, pagamentoRepository);

        // Simula retorno vazio
        when(pagamentoService.findAll()).thenReturn(List.of());

        ResponseEntity<List<PagamentoModel>> response = controller.findAll();

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }


}
