package DUA.pet.petHouse.controller;

import DUA.pet.petHouse.enums.StatusPedido;
import DUA.pet.petHouse.model.*;
import DUA.pet.petHouse.repository.EnderecoRepository;
import DUA.pet.petHouse.repository.PagamentoRepository;
import DUA.pet.petHouse.repository.PedidoRepository;
import DUA.pet.petHouse.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PedidoControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PedidoRepository repository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Autowired
    PedidoRepository pedidoRepository;

    PedidoModel pedido;
    EnderecoModel endereco;
    UsuarioModel usuario;


    @BeforeEach
    void setUp() {
        pagamentoRepository.deleteAll();
        pedidoRepository.deleteAll();
        enderecoRepository.deleteAll();
        usuarioRepository.deleteAll();
        // Criar e salvar o usuário primeiro
        UsuarioModel usuario = new UsuarioModel();
        usuario.setNome("João");
        usuario.setEmail("joao@email.com");
        usuario.setSenha("Abcd@1234");
        usuario.setUser("joaoUser");
        usuario.setCPF("12345678901");
        usuario.setDataNascimento(LocalDate.of(1990, 1, 1));
        usuario = usuarioRepository.save(usuario);

        // Criar e salvar endereço
        EnderecoModel endereco = new EnderecoModel();
        endereco.setLogradouro("Rua A");
        endereco.setNumero(123);
        endereco.setBairro("Bairro B");
        endereco.setCidade("Cidade C");
        endereco.setEstado("Estado D");
        endereco.setCep("12345678");
        endereco.setUsuario(usuario);
        endereco = enderecoRepository.save(endereco);

        // Criar pedido com usuário e endereço salvos
        pedido = new PedidoModel();
        pedido.setCliente(usuario);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setTotal(150.0);
        pedido.setProdutos(new HashSet<>());
        pedido.setEndereco(endereco);
        pedido.setStatusPedido(StatusPedido.PENDENTE);

        pedido = repository.save(pedido);
    }


    @Test
    @DisplayName("Teste API findAll PedidoController")
    void findAllTest() {
        ResponseEntity<PedidoModel[]> response = restTemplate
                .getForEntity("/pedidos/findAll", PedidoModel[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Teste erro 404 em findAll (lista vazia)")
    void findAllNotFound() {
        repository.deleteAll(); // Remove todos para simular lista vazia
        ResponseEntity<PedidoModel[]> response = restTemplate
                .getForEntity("/pedidos/findAll", PedidoModel[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(0, response.getBody().length);
    }

    @Test
    @DisplayName("Teste API findById PedidoController")
    void findById() {
        ResponseEntity<PedidoModel> response = restTemplate
                .getForEntity("/pedidos/findById/" + pedido.getId(), PedidoModel.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(pedido.getId(), response.getBody().getId());
    }

    @Test
    @DisplayName("Teste do erro 404 em findById")
    void findByIdNotFound() {
        ResponseEntity<PedidoModel> response = restTemplate
                .getForEntity("/pedidos/findById/9999", PedidoModel.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Teste API save PedidoController")
    void saveTest() {
        PedidoModel novoPedido = new PedidoModel();
        novoPedido.setCliente(usuario);
        novoPedido.setDataPedido(LocalDateTime.now());
        novoPedido.setTotal(200.0);
        novoPedido.setProdutos(new HashSet<>());
        novoPedido.setEndereco(endereco);
        novoPedido.setStatusPedido(StatusPedido.PENDENTE);

        ResponseEntity<PedidoModel> response = restTemplate
                .postForEntity("/pedidos/save", novoPedido, PedidoModel.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(200.0, response.getBody().getTotal());
    }

    @Test
    @DisplayName("Teste save com erro (campo obrigatório nulo)")
    void saveWithErrorTest() {
        PedidoModel novoPedido = new PedidoModel(); // sem dados obrigatórios
        ResponseEntity<PedidoModel> response = restTemplate
                .postForEntity("/pedidos/save", novoPedido, PedidoModel.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
